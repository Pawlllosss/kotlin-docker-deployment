package pl.oczadly.kotlin_app.hackyeah2025.pension.entity;

import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionRequest;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionResponse;

public record PensionCalculationAuditing(
        PensionRequest request,
        PensionResponse response
) {
}
