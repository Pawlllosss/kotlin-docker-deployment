package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

import lombok.Builder;

@Builder
public record AccountProgression(
    int year,
    double balanceNominal,
    double balanceReal
) {}
