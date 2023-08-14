package com.backend.goal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;

public record GoalListResponseDto(
        Long goalId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Integer entireDetailGoalCnt,
        Integer completedDetailGoalCnt
) {
}

