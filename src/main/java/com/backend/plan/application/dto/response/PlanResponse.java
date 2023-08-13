package com.backend.plan.application.dto.response;

import com.backend.plan.domain.Plan;

import java.time.LocalDate;

public record PlanResponse(Long planId, String title, LocalDate startDate, LocalDate endDate, Long dDay) {

        public static PlanResponse from(Plan plan, Long dDay)
        {
            return new PlanResponse(plan.getId(), plan.getTitle(), plan.getStartDate(), plan.getEndDate(), dDay);
        }
}
