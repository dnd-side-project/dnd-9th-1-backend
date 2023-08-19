package com.backend.goal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.goal.application.dto.response.GoalCountResponse;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.application.dto.response.RetrospectEnabledGoalCountResponse;
import com.backend.goal.domain.*;
import com.backend.goal.application.dto.response.GoalResponse;
import com.backend.goal.presentation.dto.GoalRecoverRequest;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;

    private final GoalQueryRepository goalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;


    public GoalListResponse getGoalList(Long goalId, Pageable pageable, String goalStatus)
    {
        Slice<Goal> goalList = goalQueryRepository.getGoalList(goalId, pageable, GoalStatus.from(goalStatus));
        Slice<GoalListResponseDto> result = goalList.map(GoalListResponseDto::from);
        List<GoalListResponseDto> contents = result.getContent();

        Boolean next = goalList.hasNext();
        return new GoalListResponse(contents, next);
    }

    public GoalCountResponse getGoalCounts()
    {
        Map<GoalStatus, Long> statusCounts = goalQueryRepository.getStatusCounts();
        return new GoalCountResponse(statusCounts);
    }

    public RetrospectEnabledGoalCountResponse getGoalCountRetrospectEnabled()
    {
        Long count = goalQueryRepository.getGoalCountRetrospectEnabled();
        return new RetrospectEnabledGoalCountResponse(count);
    }


    @Transactional
    public Long saveGoal(final Long memberId, final GoalSaveRequest goalSaveRequest)
    {
        Goal goal = goalSaveRequest.toEntity(memberId);
        return goalRepository.save(goal).getId();
    }

    @Transactional
    public GoalResponse updateGoal(final GoalUpdateRequest goalSaveRequest) {

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalSaveRequest.goalId());
        goal.update(goalSaveRequest.title(),goalSaveRequest.startDate(),goalSaveRequest.endDate(),goalSaveRequest.reminderEnabled());
        return GoalResponse.from(goal, goal.calculateDday(LocalDate.now()));
    }

    @Transactional
    public void removeGoal(Long goalId)
    {
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        goal.remove();

        applicationEventPublisher.publishEvent(new RemoveRelatedDetailGoalEvent(goal.getId()));
    }

    @Transactional
    public void recoverGoal(Long goalId, GoalRecoverRequest goalRecoverRequest)
    {
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        goal.recover(goalRecoverRequest.startDate(), goalRecoverRequest.endDate(), goalRecoverRequest.reminderEnabled());
    }
}
