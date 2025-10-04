package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

public record PensionRequest(
    int age,
    String sex,
    double grossSalary,
    int startYear,
    int retirementYear,
    boolean includeSickLeave,
    Double currentAccountBalance,
    Double expectedPension
) {}
