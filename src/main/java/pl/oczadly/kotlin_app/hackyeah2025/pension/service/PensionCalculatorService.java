package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oczadly.kotlin_app.hackyeah2025.pension.entity.PensionCalculationAuditing;
import pl.oczadly.kotlin_app.hackyeah2025.pension.entity.PensionCalculationAuditingRepository;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PensionCalculatorService {

    private static final double CONTRIBUTION_RATE = 0.1952;
    private static final double SICK_LEAVE_REDUCTION = 0.2;
    private static final int AVG_SICK_DAYS_MALE = 35;
    private static final int AVG_SICK_DAYS_FEMALE = 32;

    private final ForecastDataService forecastDataService;
    private final PensionCalculationAuditingRepository pensionCalculationRepository;

    public PensionResponse calculatePension(PensionRequest request) {
        // Build nominal pension details
        var nominalDetails = calculateNominalPension(request);
        // Build real pension details

        var accountBalanceWithSickLeave = calculateAccountBalance(request, true);
        var accountBalanceWithoutSickLeave = calculateAccountBalance(request, false);
        var realDetails = calculateRealPension(request, nominalDetails, accountBalanceWithSickLeave);

        // Build account progression
        var accountProgression = buildAccountProgression(request);

        var response = PensionResponse.builder()
          .nominalPension(nominalDetails)
          .realPension(realDetails)
          .accountProgression(accountProgression)
          .build();
        pensionCalculationRepository.save(new PensionCalculationAuditing(ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).toLocalDateTime(),request, response));
        return response;
    }

    private PensionDetails calculateNominalPension(PensionRequest request) {
        var accountBalanceWithSickLeave = calculateAccountBalance(request, true);
        var accountBalanceWithoutSickLeave = calculateAccountBalance(request, false);

        // Get life expectancy
        double lifeExpectancyMonths = forecastDataService.getLifeExpectancyMonths(request.retirementYear(), request.sex());

        // Calculate nominal pensions
        double nominalPensionWithSickLeave = accountBalanceWithSickLeave / lifeExpectancyMonths;
        double nominalPensionWithoutSickLeave = accountBalanceWithoutSickLeave / lifeExpectancyMonths;

        var finalSalary = calculateSalaryAfterInflation(request);
        var nominalReplacementRate = (nominalPensionWithSickLeave / finalSalary) * 100;

        var avgPensionAtRetirement = forecastDataService.getAveragePension(request.retirementYear(), request.sex());

        var nominalVsAverage = (nominalPensionWithSickLeave / avgPensionAtRetirement) * 100;
        List<DelayedScenario> nominalDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, false);

        Integer yearsNeededForExpected = null;
        if (request.expectedPension() != null) {
            yearsNeededForExpected = calculateYearsNeededForExpected(request, request.expectedPension(), false);
        }

        return PensionDetails.builder()
                .withSickLeave(nominalPensionWithSickLeave)
                .withoutSickLeave(nominalPensionWithoutSickLeave)
                .replacementRate(nominalReplacementRate)
                .vsAveragePension(nominalVsAverage)
                .delayedScenarios(nominalDelayedScenarios)
                .extraYearsNeededForExpected(yearsNeededForExpected)
                .build();
    }

    private PensionDetails calculateRealPension(PensionRequest request, PensionDetails nominalPensionDetails, double accountBalanceWithSickLeave) {
        // Calculate cumulative inflation from 2025 to retirement year
        var cumulativeInflation = forecastDataService.getCumulativeInflation(2025, request.retirementYear());

        // Calculate nominal pensions
        double nominalWithSickLeave = nominalPensionDetails.withSickLeave();
        double nominalWithoutSickLeave = nominalPensionDetails.withoutSickLeave();
        var realPensionWithSickLeave = nominalWithSickLeave / cumulativeInflation;
        var realPensionWithoutSickLeave = nominalWithoutSickLeave / cumulativeInflation;


        var finalSalary = calculateSalaryAfterInflation(request);
        var realReplacementRate = (realPensionWithSickLeave / (finalSalary / cumulativeInflation)) * 100;

        var avgPensionAtRetirement = forecastDataService.getAveragePension(request.retirementYear(), request.sex());
        var realVsAverage = (realPensionWithSickLeave / (avgPensionAtRetirement / cumulativeInflation)) * 100;

        var realDelayedScenarios = calculateDelayedScenarios(request, accountBalanceWithSickLeave, true);

        // Calculate salary needed for expected pension
        Integer extraYearsNeededForExpected = null;
        if (request.expectedPension() != null) {
            extraYearsNeededForExpected = calculateYearsNeededForExpected(request, request.expectedPension(), true);
        }


        return PensionDetails.builder()
                .withSickLeave(realPensionWithSickLeave)
                .withoutSickLeave(realPensionWithoutSickLeave)
                .replacementRate(realReplacementRate)
                .vsAveragePension(realVsAverage)
                .delayedScenarios(realDelayedScenarios)
                .extraYearsNeededForExpected(extraYearsNeededForExpected)
                .build();
    }

    private double calculateAccountBalance(PensionRequest request, boolean includeSickLeave) {
        var balance = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        var currentSalary = request.grossSalary();

        var avgSickDays = request.sex().equalsIgnoreCase("M") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
        var workDaysInYear = 250.0; // Approximate working days TODO: parameterize it

        for (var year = request.startYear(); year < request.retirementYear(); year++) {
            // TODO use forecastDataService:
//            Double valorizationRate = forecastDataService.getValorizationRate(year);
            Double valorizationRate = 1.025;
            balance *= valorizationRate;
            var yearlyContribution = currentSalary * 12 * CONTRIBUTION_RATE;

            if (includeSickLeave) {
                var sickLeaveDaysFraction = avgSickDays / workDaysInYear;
                var sickLeaveReduction = yearlyContribution * sickLeaveDaysFraction * SICK_LEAVE_REDUCTION;
                yearlyContribution -= sickLeaveReduction;
            }

            balance += yearlyContribution;

            // TODO: use it only for real
//            // Apply wage growth for next year
//            currentSalary *= (forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculateSalaryAfterInflation(PensionRequest request) {
        var salary = request.grossSalary();
        for (var year = request.startYear(); year < request.retirementYear(); year++) {
            // I think only in case of real salary we should consider wage growth
            salary /= (forecastDataService.getInflationRate(year));
        }
        return salary;
    }

    private List<DelayedScenario> calculateDelayedScenarios(PensionRequest request, double baseAccountBalance, boolean useRealValues) {
        List<DelayedScenario> scenarios = new ArrayList<>();
        var delayYears = List.of(1, 2, 5);

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
        var salary = calculateSalaryAfterInflation(request);

        for (var year = request.retirementYear(); year < request.retirementYear() + delayYears; year++) {
            balance += salary * CONTRIBUTION_RATE;
            salary *= (forecastDataService.getWageGrowth(year));
        }

        return balance;
    }

    private double calculatePensionForBalance(double balance, PensionRequest request, int delayYears, boolean calculateRealOutcome) {
        var retirementYear = request.retirementYear() + delayYears;
        var lifeExpectancyMonths = forecastDataService.getLifeExpectancyMonths(retirementYear, request.sex());

        var delayedLifeExpectancy = Math.max(1, lifeExpectancyMonths - delayYears * 12);

        double pension = balance / delayedLifeExpectancy;

        if (calculateRealOutcome) {
            // TODO include wage growth
            var cumulativeInflation = forecastDataService.getCumulativeInflation(request.startYear(), retirementYear);
            pension = pension / cumulativeInflation;
        } else {
        }

        return pension;
    }

    private int calculateYearsNeededForExpected(PensionRequest request, double expectedPension, boolean useRealValues) {
        var currentPension = calculatePensionForBalance(
                calculateAccountBalance(request, true),
                request,
                0,
                useRealValues
        );

        if (currentPension >= expectedPension) {
            return 0; // Already meets expectations
        }

        for(int extraYears = 1 ; ; extraYears++) {
            var delayedPension = calculatePensionForBalance(
                    calculateAccountBalance(request, true),
                    request,
                    extraYears,
                    useRealValues
            );


            if (delayedPension >= expectedPension) {
                return extraYears;
            }
        }

    }

    private List<AccountProgression> buildAccountProgression(PensionRequest request) {
        List<AccountProgression> progression = new ArrayList<>();
        var balanceNominal = request.currentAccountBalance() != null ? request.currentAccountBalance() : 0.0;
        var currentSalary = request.grossSalary();

        var avgSickDays = request.sex().equalsIgnoreCase("M") ? AVG_SICK_DAYS_MALE : AVG_SICK_DAYS_FEMALE;
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
            currentSalary *= (forecastDataService.getWageGrowth(year));
        }

        return progression;
    }
}
