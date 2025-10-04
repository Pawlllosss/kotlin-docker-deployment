package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

import lombok.Builder;

@Builder
public record DelayedScenario(
    int years,
    double pension,
    double increasePct
) {}
