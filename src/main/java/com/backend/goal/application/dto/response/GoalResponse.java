package com.backend.goal.application.dto.response;


import com.backend.goal.domain.Goal;

import java.time.LocalDate;

public record GoalResponse(Long goalId, String title, LocalDate startDate, LocalDate endDate, Long dDay) {

        public static GoalResponse from(Goal goal, Long dDay)
        {
            return new GoalResponse(goal.getId(), goal.getTitle(), goal.getStartDate(), goal.getEndDate(), dDay);
        }
}
