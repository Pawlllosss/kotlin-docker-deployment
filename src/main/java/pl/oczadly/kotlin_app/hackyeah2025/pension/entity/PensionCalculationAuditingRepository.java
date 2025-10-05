package pl.oczadly.kotlin_app.hackyeah2025.pension.entity;

import org.springframework.stereotype.Repository;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.*;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PensionCalculationAuditingRepository {

    Map<UUID, PensionCalculationAuditing> db = new HashMap<>();

    {
        // Mock data entry 1: Young professional, male, starting career
        db.put(UUID.randomUUID(), new PensionCalculationAuditing(
                LocalDateTime.now(),
                new PensionRequest(25, "M", 6000.0, 2025, 2060, false, null, null, null, "31-800"),
                PensionResponse.builder()
                        .nominalPension(PensionDetails.builder()
                                .withSickLeave(4500.0)
                                .withoutSickLeave(4500.0)
                                .replacementRate(45.0)
                                .finalSalary(10000.0)
                                .finalAveragePension(5500.0)
                                .vsAveragePension(81.8)
                                .delayedScenarios(List.of())
                                .extraYearsNeededForExpected(null)
                                .build())
                        .realPension(PensionDetails.builder()
                                .withSickLeave(2800.0)
                                .withoutSickLeave(2800.0)
                                .replacementRate(42.0)
                                .finalSalary(6666.67)
                                .finalAveragePension(3666.67)
                                .vsAveragePension(76.4)
                                .delayedScenarios(List.of())
                                .extraYearsNeededForExpected(null)
                                .build())
                        .accountProgression(List.of())
                        .build()
        ));

        // Mock data entry 2: Mid-career female with sick leave
        db.put(UUID.randomUUID(), new PensionCalculationAuditing(
                LocalDateTime.now(),
                new PensionRequest(40, "F", 8000.0, 2010, 2040, true, 32, 50000.0, 5000.0, "41-700"),
                PensionResponse.builder()
                        .nominalPension(PensionDetails.builder()
                                .withSickLeave(6200.0)
                                .withoutSickLeave(6500.0)
                                .replacementRate(52.0)
                                .finalSalary(11900.0)
                                .finalAveragePension(6200.0)
                                .vsAveragePension(100.0)
                                .delayedScenarios(List.of(
                                        new DelayedScenario(1, 6500.0, 4.8),
                                        new DelayedScenario(2, 6800.0, 9.7),
                                        new DelayedScenario(5, 7500.0, 21.0)
                                ))
                                .extraYearsNeededForExpected(0)
                                .build())
                        .realPension(PensionDetails.builder()
                                .withSickLeave(3900.0)
                                .withoutSickLeave(4100.0)
                                .replacementRate(49.0)
                                .finalSalary(7960.0)
                                .finalAveragePension(4130.0)
                                .vsAveragePension(94.4)
                                .delayedScenarios(List.of(
                                        new DelayedScenario(1, 4050.0, 3.8),
                                        new DelayedScenario(2, 4200.0, 7.7),
                                        new DelayedScenario(5, 4650.0, 19.2)
                                ))
                                .extraYearsNeededForExpected(2)
                                .build())
                        .accountProgression(List.of())
                        .build()
        ));

        // Mock data entry 3: Senior professional male, high earner
        db.put(UUID.randomUUID(), new PensionCalculationAuditing(
                LocalDateTime.now(),
                new PensionRequest(55, "M", 15000.0, 2002, 2067, false, null, 200000.0, 10000.0, "31-860"),
                PensionResponse.builder()
                        .nominalPension(PensionDetails.builder()
                                .withSickLeave(12000.0)
                                .withoutSickLeave(12000.0)
                                .replacementRate(58.0)
                                .finalSalary(20690.0)
                                .finalAveragePension(7800.0)
                                .vsAveragePension(153.8)
                                .delayedScenarios(List.of(
                                        new DelayedScenario(1, 12500.0, 4.2),
                                        new DelayedScenario(2, 13000.0, 8.3),
                                        new DelayedScenario(5, 14500.0, 20.8)
                                ))
                                .extraYearsNeededForExpected(0)
                                .build())
                        .realPension(PensionDetails.builder()
                                .withSickLeave(6500.0)
                                .withoutSickLeave(6500.0)
                                .replacementRate(54.0)
                                .finalSalary(12040.0)
                                .finalAveragePension(4540.0)
                                .vsAveragePension(143.2)
                                .delayedScenarios(List.of(
                                        new DelayedScenario(1, 6750.0, 3.8),
                                        new DelayedScenario(2, 7000.0, 7.7),
                                        new DelayedScenario(5, 7800.0, 20.0)
                                ))
                                .extraYearsNeededForExpected(0)
                                .build())
                        .accountProgression(List.of())
                        .build()
        ));
    }

    public PensionCalculationAuditing save(PensionCalculationAuditing forecastResult) {
        db.put(UUID.randomUUID(), forecastResult);

        return forecastResult;
    }

    public List<PensionCalculationAuditing> getAll() {
        return db.values().stream().toList();
    }

}
