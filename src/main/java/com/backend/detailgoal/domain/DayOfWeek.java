package com.backend.detailgoal.domain;

public enum DayOfWeek {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일");

    DayOfWeek(String description)
    {
        this.description = description;
    }
    private String description;

    public String getDescription()
    {
        return description;
    }

}
