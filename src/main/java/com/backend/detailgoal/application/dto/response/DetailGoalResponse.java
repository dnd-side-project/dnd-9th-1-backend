package com.backend.detailgoal.application.dto.response;

import com.backend.detailgoal.domain.DetailGoal;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record DetailGoalResponse(Long detailGoalId, String title, LocalTime localTime, Set<DayOfWeek> days, Boolean isCompleted)
{
    public static DetailGoalResponse from(DetailGoal detailGoal)
    {
        return new DetailGoalResponse(
                detailGoal.getId(),
                detailGoal.getTitle(),
                detailGoal.getAlarmTime(),
                detailGoal.getAlarmDays(),
                detailGoal.getIsCompleted()
        );
    }
}
