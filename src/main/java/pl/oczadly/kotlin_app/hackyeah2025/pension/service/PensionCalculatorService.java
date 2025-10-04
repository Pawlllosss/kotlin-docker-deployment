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
    private static final double SICK_LEAVE_REDUCTION = 0.25;
    private static final int AVG_SICK_DAYS_MALE = 10;
    private static final int AVG_SICK_DAYS_FEMALE = 14;

    private final ForecastDataService forecastDataService;

    public PensionResponse calculatePension(PensionRequest request) {
        // Calculate account balances
        double accountBalanceWithSickLeave = calculateAccountBalance(request, true);
        double accountBalanceWithoutSickLeave = calculateAccountBalance(request, false);

        // Get life expectancy
        double lifeExpectancyYears = forecastDataService.getLifeExpectancy(request.retirementYear(), request.sex());
        double lifeExpectancyMonths = lifeExpectancyYears * 12;

        // Calculate nominal pensions
        double nominalPensionWithSickLeave = accountBalanceWithSickLeave / lifeExpectancyMonths;
        double nominalPensionWithoutSickLeave = accountBalanceWithoutSickLeave / lifeExpectancyMonths;

        // Calculate cumulative inflation from 2025 to retirement year
        double cumulativeInflation = forecastDataService.getCumulativeInflation(2025, request.retirementYear());

        // Calculate real pensions
        double realPensionWithSickLeave = nominalPensionWithSickLeave / cumulativeInflation;
        double realPensionWithoutSickLeave = nominalPensionWithoutSickLeave / cumulativeInflation;

        // Calculate replacement rate (pension / final salary)
        double finalSalary = calculateFinalSalary(request);
        double nominalReplacementRate = (nominalPensionWithSickLeave / finalSalary) * 100;
        double realReplacementRate = (realPensionWithSickLeave / (finalSalary / cumulativeInflation)) * 100;

        // Get average pension at retirement year
        double avgPensionAtRetirement = forecastDataService.getAveragePension(request.retirementYear());
        double nominalVsAverage = (nominalPensionWithSickLeave / avgPensionAtRetirement) * 100;
        double realVsAverage = (realPensionWithSickLeave / (avgPensionAtRetirement / cumulativeInflation)) * 100;

        // Calculate delayed scenarios
        List<DelayedScenario> nominalDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, false);
        List<DelayedScenario> realDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, true);

        // Calculate salary needed for expected pension
        Double nominalSalaryNeeded = null;
        Double realSalaryNeeded = null;
        if (request.expectedPension() != null) {
            nominalSalaryNeeded = calculateSalaryNeededForExpected(request, request.expectedPension(), false);
            realSalaryNeeded = calculateSalaryNeededForExpected(request, request.expectedPension(), true);
        }

        // Build nominal pension details
        PensionDetails nominalDetails = PensionDetails.builder()
                .withSickLeave(nominalPensionWithSickLeave)
                .withoutSickLeave(nominalPensionWithoutSickLeave)
                .replacementRate(nominalReplacementRate)
                .vsAveragePension(nominalVsAverage)
                .delayedScenarios(nominalDelayedScenarios)
                .salaryNeededForExpected(nominalSalaryNeeded)
                .build();

        // Build real pension details
        PensionDetails realDetails = PensionDetails.builder()
                .withSickLeave(realPensionWithSickLeave)
                .withoutSickLeave(realPensionWithoutSickLeave)
                .replacementRate(realReplacementRate)
                .vsAveragePension(realVsAverage)
                .delayedScenarios(realDelayedScenarios)
                .salaryNeededForExpected(realSalaryNeeded)
                .build();

        // Build account progression
        List<AccountProgression> accountProgression = buildAccountProgression(request);

        return PensionResponse.builder()
                .nominalPension(nominalDetails)
                .realPension(realDetails)
                .accountProgression(accountProgression)
                .build();
    }

    private double calculateAccountBalance(PensionRequest request, boolean includeSickLeave) {
        double balance = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        double currentSalary = request.grossSalary();

        int avgSickDays = request.sex().equalsIgnoreCase("male") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
        double workDaysInYear = 250.0; // Approximate working days

        for (int year = request.startYear(); year < request.retirementYear(); year++) {
            double yearlyContribution = currentSalary * CONTRIBUTION_RATE;

            if (includeSickLeave && request.includeSickLeave()) {
                double sickLeaveDaysFraction = avgSickDays / workDaysInYear;
                double sickLeaveReduction = yearlyContribution * sickLeaveDaysFraction * SICK_LEAVE_REDUCTION;
                yearlyContribution -= sickLeaveReduction;
            }

            balance += yearlyContribution;

            // Apply wage growth for next year
            currentSalary *= (1 + forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculateFinalSalary(PensionRequest request) {
        double salary = request.grossSalary();
        for (int year = request.startYear(); year < request.retirementYear(); year++) {
            salary *= (1 + forecastDataService.getWageGrowth(year));
        }
        return salary;
    }

    private List<DelayedScenario> calculateDelayedScenarios(PensionRequest request, double baseAccountBalance, boolean useRealValues) {
        List<DelayedScenario> scenarios = new ArrayList<>();
        int[] delayYears = {1, 2, 5};

        double basePension = calculatePensionForBalance(baseAccountBalance, request, 0, useRealValues);

        for (int delay : delayYears) {
            double delayedBalance = calculateDelayedAccountBalance(request, baseAccountBalance, delay);
            double delayedPension = calculatePensionForBalance(delayedBalance, request, delay, useRealValues);
            double increasePct = ((delayedPension - basePension) / basePension) * 100;

            scenarios.add(DelayedScenario.builder()
                    .years(delay)
                    .pension(delayedPension)
                    .increasePct(increasePct)
                    .build());
        }

        return scenarios;
    }

    private double calculateDelayedAccountBalance(PensionRequest request, double baseBalance, int delayYears) {
        double balance = baseBalance;
        double salary = calculateFinalSalary(request);

        for (int year = request.retirementYear(); year < request.retirementYear() + delayYears; year++) {
            balance += salary * CONTRIBUTION_RATE;
            salary *= (1 + forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculatePensionForBalance(double balance, PensionRequest request, int delayYears, boolean useRealValues) {
        int retirementYear = request.retirementYear() + delayYears;
        double lifeExpectancy = forecastDataService.getLifeExpectancy(retirementYear, request.sex());

        // Reduce life expectancy by delay years
        lifeExpectancy = Math.max(1, lifeExpectancy - delayYears);
        double lifeExpectancyMonths = lifeExpectancy * 12;

        double pension = balance / lifeExpectancyMonths;

        if (useRealValues) {
            double cumulativeInflation = forecastDataService.getCumulativeInflation(2025, retirementYear);
            pension = pension / cumulativeInflation;
        }

        return pension;
    }

    private Double calculateSalaryNeededForExpected(PensionRequest request, double expectedPension, boolean useRealValues) {
        double currentPension = calculatePensionForBalance(
                calculateAccountBalance(request, true),
                request,
                0,
                useRealValues
        );

        if (currentPension >= expectedPension) {
            return null; // Already meets expectations
        }

        double multiplier = expectedPension / currentPension;
        return request.grossSalary() * multiplier;
    }

    private List<AccountProgression> buildAccountProgression(PensionRequest request) {
        List<AccountProgression> progression = new ArrayList<>();
        double balanceNominal = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        double currentSalary = request.grossSalary();

        int avgSickDays = request.sex().equalsIgnoreCase("male") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
        double workDaysInYear = 250.0;

        for (int year = request.startYear(); year < request.retirementYear(); year++) {
            double yearlyContribution = currentSalary * CONTRIBUTION_RATE;

            if (request.includeSickLeave()) {
                double sickLeaveDaysFraction = avgSickDays / workDaysInYear;
                double sickLeaveReduction = yearlyContribution * sickLeaveDaysFraction * SICK_LEAVE_REDUCTION;
                yearlyContribution -= sickLeaveReduction;
            }

            balanceNominal += yearlyContribution;

            // Calculate real balance
            double cumulativeInflation = forecastDataService.getCumulativeInflation(2025, year);
            double balanceReal = balanceNominal / cumulativeInflation;

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
