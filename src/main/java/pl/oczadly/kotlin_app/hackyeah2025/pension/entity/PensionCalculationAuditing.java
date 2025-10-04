package pl.oczadly.kotlin_app.hackyeah2025.pension.entity;

import pl.oczadly.kotlin_app.hackyeah2025.pension.model.AccountProgression;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionDetails;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionRequest;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PensionCalculationAuditing(
    LocalDateTime calculatedAt, PensionRequest request, PensionResponse response) {}
