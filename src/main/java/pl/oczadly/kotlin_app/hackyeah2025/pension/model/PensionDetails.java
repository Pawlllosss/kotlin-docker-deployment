package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PensionDetails(
    double withSickLeave,
    double withoutSickLeave,
    double replacementRate,
    double vsAveragePension,
    List<DelayedScenario> delayedScenarios,
    Integer extraYearsNeededForExpected
) {}
