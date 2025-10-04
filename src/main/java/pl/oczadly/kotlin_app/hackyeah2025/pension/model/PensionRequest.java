package pl.oczadly.kotlin_app.hackyeah2025.pension.model;

public record PensionRequest(
    Integer age,
    String sex,
    Double grossSalary,
    Integer startYear,
    Integer retirementYear,
    boolean includeSickLeave,
    Integer avgSickDaysPerYear,
    Double currentAccountBalance,
    Double expectedPension,
    String zipCode) {}
