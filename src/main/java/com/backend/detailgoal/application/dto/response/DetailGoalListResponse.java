package com.backend.detailgoal.application.dto.response;

import com.backend.detailgoal.domain.DetailGoal;

import java.time.LocalTime;
import java.util.List;

public record DetailGoalListResponse(Long detailGoalId, String title, Boolean isCompleted)
{
    public static DetailGoalListResponse from(DetailGoal detailGoal)
    {
        return new DetailGoalListResponse(
                detailGoal.getId(),
                detailGoal.getTitle(),
                detailGoal.getIsCompleted()
        );
    }
}
