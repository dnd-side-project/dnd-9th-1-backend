package com.backend.goal.domain;


import java.time.LocalDate;

public record GoalListResponseDto(
        Long goalId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Integer detailGoalCnt,
        Long dDay,
        Boolean hasRetrospect
) {

    public static GoalListResponseDto from(Goal goal)
    {
        return new GoalListResponseDto(goal.getId(),
                goal.getTitle(),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.getDetailGoalCnt(),
                goal.calculateDday(LocalDate.now()),
                goal.getHasRetrospect()
        )
                ;
    }
}

