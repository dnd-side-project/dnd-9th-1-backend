package com.backend.plan.application;

import com.backend.plan.domain.Plan;
import com.backend.plan.domain.PlanRepository;
import com.backend.plan.application.dto.response.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    public PlanResponse getPlan(Long planId)
    {
        Plan plan = planRepository.getById(planId);
        Long dDay = plan.calculateDday(LocalDate.now());
        return PlanResponse.from(plan, dDay);
    }
}
