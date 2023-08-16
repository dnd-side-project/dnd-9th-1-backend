package com.backend.detailgoal.presentation.dto.request;

import com.backend.detailgoal.domain.DetailGoal;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public record DetailGoalSaveRequest(

        String title,
        Boolean alarmEnabled,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime alarmTime,
        List<String> alarmDays
) {

    public DetailGoal toEntity()
    {
        List<DayOfWeek> days = alarmDays.stream().map(DayOfWeek::valueOf).collect(Collectors.toList());
        return DetailGoal.builder()
                .title(title)
                .alarmEnabled(alarmEnabled)
                .alarmTime(alarmTime)
                .alarmDays(days)
                .build();
    }
}
