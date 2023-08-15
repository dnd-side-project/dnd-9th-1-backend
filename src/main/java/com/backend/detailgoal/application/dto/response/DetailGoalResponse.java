package com.backend.detailgoal.application.dto.response;

import com.backend.detailgoal.domain.DetailGoal;

import java.time.LocalTime;
import java.util.List;

public record DetailGoalResponse(Long detailGoalId, String title, LocalTime localTime, List<String> days)
{
    public static DetailGoalResponse from(DetailGoal detailGoal)
    {
        return new DetailGoalResponse(
                detailGoal.getId(),
                detailGoal.getTitle(),
                detailGoal.getAlarmTime(),
                detailGoal.extractDayName()
        );
    }
}
