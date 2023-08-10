package com.backend.plan.domain;

public enum PlanStatus {

    STORE("보관함"),
    PROCESS("채움함"),
    COMPLETE("완료함");

    private String description;

    PlanStatus(String description)
    {
        this.description = description;
    }

    public static PlanStatus from(final String value) {
        try {
            return PlanStatus.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
