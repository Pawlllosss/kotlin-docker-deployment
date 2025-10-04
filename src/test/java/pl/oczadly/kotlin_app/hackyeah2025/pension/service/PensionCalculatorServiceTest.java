package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PensionCalculatorServiceTest {

    @Mock
    private ForecastDataService forecastDataService;

    @InjectMocks
    private PensionCalculatorService pensionCalculatorService;

    @BeforeEach
    void setUp() {
        when(forecastDataService.getWageGrowth(anyInt())).thenReturn(0.03);
        when(forecastDataService.getLifeExpectancy(anyInt(), anyString())).thenReturn(20.0);
        when(forecastDataService.getAveragePension(anyInt())).thenReturn(3500.0);
        when(forecastDataService.getCumulativeInflation(anyInt(), anyInt())).thenReturn(1.5);
    }

    @Test
    void shouldCalculatePensionWithBasicScenario() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.nominalPension()).isNotNull();
        assertThat(response.realPension()).isNotNull();
        assertThat(response.accountProgression()).isNotEmpty();
        assertThat(response.accountProgression()).hasSize(40);
    }

    @Test
    void shouldCalculateDifferentPensionsWithAndWithoutSickLeave() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                true,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().withSickLeave())
                .isLessThan(response.nominalPension().withoutSickLeave());
        assertThat(response.realPension().withSickLeave())
                .isLessThan(response.realPension().withoutSickLeave());
    }

    @Test
    void shouldCalculateSamePensionWhenSickLeaveNotIncluded() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().withSickLeave())
                .isEqualTo(response.nominalPension().withoutSickLeave());
        assertThat(response.realPension().withSickLeave())
                .isEqualTo(response.realPension().withoutSickLeave());
    }

    @Test
    void shouldUseDifferentSickDaysForMaleAndFemale() {
        // given
        PensionRequest maleRequest = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                true,
                null,
                null
        );

        PensionRequest femaleRequest = new PensionRequest(
                30,
                "female",
                5000.0,
                2025,
                2065,
                true,
                null,
                null
        );

        // when
        PensionResponse maleResponse = pensionCalculatorService.calculatePension(maleRequest);
        PensionResponse femaleResponse = pensionCalculatorService.calculatePension(femaleRequest);

        // then - female should have lower pension due to more sick days (14 vs 10)
        assertThat(femaleResponse.nominalPension().withSickLeave())
                .isLessThan(maleResponse.nominalPension().withSickLeave());
    }

    @Test
    void shouldUseCurrentAccountBalanceWhenProvided() {
        // given
        PensionRequest requestWithBalance = new PensionRequest(
                40,
                "male",
                5000.0,
                2025,
                2065,
                false,
                100000.0,
                null
        );

        PensionRequest requestWithoutBalance = new PensionRequest(
                40,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse responseWithBalance = pensionCalculatorService.calculatePension(requestWithBalance);
        PensionResponse responseWithoutBalance = pensionCalculatorService.calculatePension(requestWithoutBalance);

        // then
        assertThat(responseWithBalance.nominalPension().withSickLeave())
                .isGreaterThan(responseWithoutBalance.nominalPension().withSickLeave());
    }

    @Test
    void shouldCalculateReplacementRate() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().replacementRate()).isPositive();
        assertThat(response.realPension().replacementRate()).isPositive();
    }

    @Test
    void shouldCalculateVsAveragePension() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().vsAveragePension()).isPositive();
        assertThat(response.realPension().vsAveragePension()).isPositive();
    }

    @Test
    void shouldCalculateDelayedScenarios() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().delayedScenarios()).hasSize(3);
        assertThat(response.realPension().delayedScenarios()).hasSize(3);

        // Verify delay years are 1, 2, and 5
        assertThat(response.nominalPension().delayedScenarios())
                .extracting(DelayedScenario::years)
                .containsExactly(1, 2, 5);

        // Verify delayed pensions increase with delay
        double basePension = response.nominalPension().withSickLeave();
        for (DelayedScenario scenario : response.nominalPension().delayedScenarios()) {
            assertThat(scenario.pension()).isGreaterThan(basePension);
            assertThat(scenario.increasePct()).isPositive();
        }
    }

    @Test
    void shouldCalculateSalaryNeededForExpectedPension() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                10000.0
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().salaryNeededForExpected()).isNotNull();
        assertThat(response.nominalPension().salaryNeededForExpected()).isGreaterThan(request.grossSalary());
        assertThat(response.realPension().salaryNeededForExpected()).isNotNull();
    }

    @Test
    void shouldReturnNullSalaryNeededWhenExpectedPensionNotProvided() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().salaryNeededForExpected()).isNull();
        assertThat(response.realPension().salaryNeededForExpected()).isNull();
    }

    @Test
    void shouldReturnNullSalaryNeededWhenExpectationAlreadyMet() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                50000.0,  // High salary
                2025,
                2065,
                false,
                null,
                1000.0    // Low expected pension
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.nominalPension().salaryNeededForExpected()).isNull();
        assertThat(response.realPension().salaryNeededForExpected()).isNull();
    }

    @Test
    void shouldCalculateAccountProgressionCorrectly() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2030,  // Short period for easier verification
                false,
                0.0,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response.accountProgression()).hasSize(5);
        assertThat(response.accountProgression().get(0).year()).isEqualTo(2025);
        assertThat(response.accountProgression().get(4).year()).isEqualTo(2029);

        // Verify progression is increasing
        for (int i = 1; i < response.accountProgression().size(); i++) {
            assertThat(response.accountProgression().get(i).balanceNominal())
                    .isGreaterThan(response.accountProgression().get(i - 1).balanceNominal());
        }
    }

    @Test
    void shouldHandleShortWorkingPeriod() {
        // given
        PensionRequest request = new PensionRequest(
                60,
                "male",
                5000.0,
                2025,
                2030,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accountProgression()).hasSize(5);
        assertThat(response.nominalPension().withSickLeave()).isPositive();
    }

    @Test
    void shouldHandleLongWorkingPeriod() {
        // given
        PensionRequest request = new PensionRequest(
                20,
                "male",
                5000.0,
                2025,
                2070,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.accountProgression()).hasSize(45);
        assertThat(response.nominalPension().withSickLeave()).isPositive();
    }

    @Test
    void shouldDifferentiateBetweenNominalAndRealValues() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                null,
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        // Real values should be lower than nominal due to inflation
        assertThat(response.realPension().withSickLeave())
                .isLessThan(response.nominalPension().withSickLeave());
        assertThat(response.realPension().withoutSickLeave())
                .isLessThan(response.nominalPension().withoutSickLeave());

        // Check account progression
        AccountProgression lastProgression = response.accountProgression().get(response.accountProgression().size() - 1);
        assertThat(lastProgression.balanceReal()).isLessThan(lastProgression.balanceNominal());
    }

    @Test
    void shouldHandleEdgeCaseWithZeroSalary() {
        // given
        PensionRequest request = new PensionRequest(
                30,
                "male",
                0.0,
                2025,
                2065,
                false,
                1000.0,  // Only account balance
                null
        );

        // when
        PensionResponse response = pensionCalculatorService.calculatePension(request);

        // then
        assertThat(response).isNotNull();
        // Pension should be based only on existing account balance
        assertThat(response.nominalPension().withSickLeave()).isPositive();
    }

    @Test
    void shouldUseCorrectLifeExpectancyForDifferentGenders() {
        // given
        when(forecastDataService.getLifeExpectancy(anyInt(), anyString())).thenAnswer(invocation -> {
            String sex = invocation.getArgument(1);
            return sex.equalsIgnoreCase("male") ? 19.0 : 24.0;
        });

        PensionRequest maleRequest = new PensionRequest(
                30,
                "male",
                5000.0,
                2025,
                2065,
                false,
                100000.0,
                null
        );

        PensionRequest femaleRequest = new PensionRequest(
                30,
                "female",
                5000.0,
                2025,
                2065,
                false,
                100000.0,
                null
        );

        // when
        PensionResponse maleResponse = pensionCalculatorService.calculatePension(maleRequest);
        PensionResponse femaleResponse = pensionCalculatorService.calculatePension(femaleRequest);

        // then - with same balance, males should get higher monthly pension due to shorter life expectancy
        assertThat(maleResponse.nominalPension().withSickLeave())
                .isGreaterThan(femaleResponse.nominalPension().withSickLeave());
    }
}
