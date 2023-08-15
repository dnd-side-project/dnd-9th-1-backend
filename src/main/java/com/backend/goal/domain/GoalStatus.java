package com.backend.goal.domain;

public enum GoalStatus {

    STORE("보관함"),
    PROCESS("채움함"),
    COMPLETE("완료함");

    private String description;

    GoalStatus(String description)
    {
        this.description = description;
    }

    public static GoalStatus from(final String value) {
        try {
            return GoalStatus.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    public String getDescription()
    {
        return description;
    }
}
