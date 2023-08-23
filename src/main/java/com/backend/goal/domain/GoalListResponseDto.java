package com.backend.goal.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;

public record GoalListResponseDto(

        Long goalId,

        String title,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,

        Integer entireDetailGoalCnt,

        Integer completedDetailGoalCnt,

        Long dDay,

        Boolean hasRetrospect,

        RewardType reward
) {

    public static GoalListResponseDto from(Goal goal)
    {
        return new GoalListResponseDto(goal.getId(),
                goal.getTitle(),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.getEntireDetailGoalCnt(),
                goal.getCompletedDetailGoalCnt(),
                goal.calculateDday(LocalDate.now()),
                goal.getHasRetrospect(),
                goal.getReward()
        )
                ;
    }
}

