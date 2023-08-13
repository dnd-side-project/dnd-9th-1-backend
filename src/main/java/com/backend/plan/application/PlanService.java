package com.backend.plan.application;

import com.backend.plan.domain.Plan;
import com.backend.plan.domain.PlanRepository;
import com.backend.plan.application.dto.response.PlanResponse;
import com.backend.plan.presentation.dto.PlanSaveRequest;
import com.backend.plan.presentation.dto.PlanUpdateRequest;
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

    @Transactional
    public void savePlan(final Long memberId, final PlanSaveRequest planSaveRequest)
    {
        Plan plan = planSaveRequest.toEntity(memberId);
        planRepository.save(plan);
    }

    @Transactional
    public void updatePlan(final PlanUpdateRequest planSaveRequest) {

        Plan plan = planRepository.getById(planSaveRequest.planId());
        plan.update(planSaveRequest.title(),planSaveRequest.startDate(),planSaveRequest.endDate(),planSaveRequest.reminderEnabled());
    }

    @Transactional
    public void removePlan(Long planId)
    {
        Plan plan = planRepository.getById(planId);
        plan.remove();
    }
}
