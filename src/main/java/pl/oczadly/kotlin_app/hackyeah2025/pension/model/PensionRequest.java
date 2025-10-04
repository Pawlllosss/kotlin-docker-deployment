package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

public record PensionRequest(
    Integer age,
    String sex,
    Double grossSalary,
    Integer startYear,
    Integer retirementYear,
    Boolean includeSickLeave,
    Integer averageSickDaysPerYear,
    Double currentAccountBalance,
    Double expectedPension
) {}
