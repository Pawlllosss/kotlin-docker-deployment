package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PensionCalculatorService {

    private static final double CONTRIBUTION_RATE = 0.1976;
    private static final double SICK_LEAVE_REDUCTION = 0.2;
    private static final int AVG_SICK_DAYS_MALE = 10;
    private static final int AVG_SICK_DAYS_FEMALE = 14;

    private final ForecastDataService forecastDataService;

    public PensionResponse calculatePension(PensionRequest request) {
        // Calculate account balances
        var accountBalanceWithSickLeave = calculateAccountBalance(request, true);
        var accountBalanceWithoutSickLeave = calculateAccountBalance(request, false);

        // Get life expectancy
        var lifeExpectancyYears = forecastDataService.getLifeExpectancy(request.retirementYear(), request.sex());
        var lifeExpectancyMonths = lifeExpectancyYears * 12;

        // Calculate nominal pensions
        var nominalPensionWithSickLeave = accountBalanceWithSickLeave / lifeExpectancyMonths;
        var nominalPensionWithoutSickLeave = accountBalanceWithoutSickLeave / lifeExpectancyMonths;

        // Calculate cumulative inflation from 2025 to retirement year
        var cumulativeInflation = forecastDataService.getCumulativeInflation(2025, request.retirementYear());

        // Calculate real pensions
        var realPensionWithSickLeave = nominalPensionWithSickLeave / cumulativeInflation;
        var realPensionWithoutSickLeave = nominalPensionWithoutSickLeave / cumulativeInflation;

        // Calculate replacement rate (pension / final salary)
        var finalSalary = calculateFinalSalary(request);
        var nominalReplacementRate = (nominalPensionWithSickLeave / finalSalary) * 100;
        var realReplacementRate = (realPensionWithSickLeave / (finalSalary / cumulativeInflation)) * 100;

        // Get average pension at retirement year
        var avgPensionAtRetirement = forecastDataService.getAveragePension(request.retirementYear());
        var nominalVsAverage = (nominalPensionWithSickLeave / avgPensionAtRetirement) * 100;
        var realVsAverage = (realPensionWithSickLeave / (avgPensionAtRetirement / cumulativeInflation)) * 100;

        // Calculate delayed scenarios
        var nominalDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, false);
        var realDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, true);

        // Calculate salary needed for expected pension
        Double nominalSalaryNeeded = null;
        Double realSalaryNeeded = null;
        if (request.expectedPension() != null) {
            nominalSalaryNeeded = calculateSalaryNeededForExpected(request, request.expectedPension(), false);
            realSalaryNeeded = calculateSalaryNeededForExpected(request, request.expectedPension(), true);
        }

        // Build nominal pension details
        var nominalDetails = PensionDetails.builder()
                .withSickLeave(nominalPensionWithSickLeave)
                .withoutSickLeave(nominalPensionWithoutSickLeave)
                .replacementRate(nominalReplacementRate)
                .vsAveragePension(nominalVsAverage)
                .delayedScenarios(nominalDelayedScenarios)
                .salaryNeededForExpected(nominalSalaryNeeded)
                .build();

        // Build real pension details
        var realDetails = PensionDetails.builder()
                .withSickLeave(realPensionWithSickLeave)
                .withoutSickLeave(realPensionWithoutSickLeave)
                .replacementRate(realReplacementRate)
                .vsAveragePension(realVsAverage)
                .delayedScenarios(realDelayedScenarios)
                .salaryNeededForExpected(realSalaryNeeded)
                .build();

        // Build account progression
        var accountProgression = buildAccountProgression(request);

        return PensionResponse.builder()
                .nominalPension(nominalDetails)
                .realPension(realDetails)
                .accountProgression(accountProgression)
                .build();
    }

    private double calculateAccountBalance(PensionRequest request, boolean includeSickLeave) {
        var balance = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        var currentSalary = request.grossSalary();

        var avgSickDays = request.sex().equalsIgnoreCase("male") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
        var workDaysInYear = 250.0; // Approximate working days

        for (var year = request.startYear(); year < request.retirementYear(); year++) {
            var yearlyContribution = currentSalary * CONTRIBUTION_RATE;

            if (includeSickLeave && request.includeSickLeave()) {
                var sickLeaveDaysFraction = avgSickDays / workDaysInYear;
                var sickLeaveReduction = yearlyContribution * sickLeaveDaysFraction * SICK_LEAVE_REDUCTION;
                yearlyContribution -= sickLeaveReduction;
            }

            balance += yearlyContribution;

            // Apply wage growth for next year
            currentSalary *= (1 + forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculateFinalSalary(PensionRequest request) {
        var salary = request.grossSalary();
        for (var year = request.startYear(); year < request.retirementYear(); year++) {
            salary *= (1 + forecastDataService.getWageGrowth(year));
        }
        return salary;
    }

    private List<DelayedScenario> calculateDelayedScenarios(PensionRequest request, double baseAccountBalance, boolean useRealValues) {
        List<DelayedScenario> scenarios = new ArrayList<>();
        var delayYears = new int[]{1, 2, 5};

        var basePension = calculatePensionForBalance(baseAccountBalance, request, 0, useRealValues);

        for (var delay : delayYears) {
            var delayedBalance = calculateDelayedAccountBalance(request, baseAccountBalance, delay);
            var delayedPension = calculatePensionForBalance(delayedBalance, request, delay, useRealValues);
            var increasePct = ((delayedPension - basePension) / basePension) * 100;

            scenarios.add(DelayedScenario.builder()
                    .years(delay)
                    .pension(delayedPension)
                    .increasePct(increasePct)
                    .build());
        }

        return scenarios;
    }

    private double calculateDelayedAccountBalance(PensionRequest request, double baseBalance, int delayYears) {
        var balance = baseBalance;
        var salary = calculateFinalSalary(request);

        for (var year = request.retirementYear(); year < request.retirementYear() + delayYears; year++) {
            balance += salary * CONTRIBUTION_RATE;
            salary *= (1 + forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculatePensionForBalance(double balance, PensionRequest request, int delayYears, boolean useRealValues) {
        var retirementYear = request.retirementYear() + delayYears;
        var lifeExpectancy = forecastDataService.getLifeExpectancy(retirementYear, request.sex());

        // Reduce life expectancy by delay years
        lifeExpectancy = Math.max(1, lifeExpectancy - delayYears);
        var lifeExpectancyMonths = lifeExpectancy * 12;

        var pension = balance / lifeExpectancyMonths;

        if (useRealValues) {
            var cumulativeInflation = forecastDataService.getCumulativeInflation(2025, retirementYear);
            pension = pension / cumulativeInflation;
        }

        return pension;
    }

    private Double calculateSalaryNeededForExpected(PensionRequest request, double expectedPension, boolean useRealValues) {
        var currentPension = calculatePensionForBalance(
                calculateAccountBalance(request, true),
                request,
                0,
                useRealValues
        );

        if (currentPension >= expectedPension) {
            return null; // Already meets expectations
        }

        var multiplier = expectedPension / currentPension;
        return request.grossSalary() * multiplier;
    }

    private List<AccountProgression> buildAccountProgression(PensionRequest request) {
        List<AccountProgression> progression = new ArrayList<>();
        var balanceNominal = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        var currentSalary = request.grossSalary();

        var avgSickDays = request.sex().equalsIgnoreCase("male") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
        var workDaysInYear = 250.0;

        for (var year = request.startYear(); year < request.retirementYear(); year++) {
            var yearlyContribution = currentSalary * CONTRIBUTION_RATE;

            if (request.includeSickLeave()) {
                var sickLeaveDaysFraction = avgSickDays / workDaysInYear;
                var sickLeaveReduction = yearlyContribution * sickLeaveDaysFraction * SICK_LEAVE_REDUCTION;
                yearlyContribution -= sickLeaveReduction;
            }

            balanceNominal += yearlyContribution;

            // Calculate real balance
            var cumulativeInflation = forecastDataService.getCumulativeInflation(2025, year);
            var balanceReal = balanceNominal / cumulativeInflation;

            progression.add(AccountProgression.builder()
                    .year(year)
                    .balanceNominal(balanceNominal)
                    .balanceReal(balanceReal)
                    .build());

            // Apply wage growth for next year
            currentSalary *= (1 + forecastDataService.getWageGrowth(year));
        }

        return progression;
    }
}
