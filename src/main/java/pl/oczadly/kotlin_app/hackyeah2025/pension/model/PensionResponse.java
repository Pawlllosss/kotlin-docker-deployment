package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PensionResponse(
    PensionDetails nominalPension,
    PensionDetails realPension,
    List<AccountProgression> accountProgression
) {}
