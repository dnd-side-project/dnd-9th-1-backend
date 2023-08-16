package com.backend.detailgoal.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.util.List;

public record DetailGoalUpdateRequest(

        String title,

        Boolean alarmEnabled,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime alarmTime,

        List<String> alarmDays
) {
}
