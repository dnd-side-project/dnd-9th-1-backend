package com.backend.detailgoal.application.dto.response;

import com.backend.goal.domain.RewardType;

public record GoalCompletedResponse(
        Boolean isGoalCompleted,
        RewardType rewardType,
        Integer completedGoalCount

) {
}
