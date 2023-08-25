package com.backend.goal.application.dto.response;


import com.backend.goal.domain.Goal;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record GoalResponse(

        Long goalId,
        String title,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        Long dDay

) {

        public static GoalResponse from(Goal goal, Long dDay)
        {
            return new GoalResponse(goal.getId(), goal.getTitle(), goal.getStartDate(), goal.getEndDate(), dDay);
        }
}
