package com.backend.goal.application;


import com.backend.goal.application.dto.response.GoalCountResponse;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.application.dto.response.RetrospectEnabledGoalCountResponse;
import com.backend.goal.domain.*;
import com.backend.goal.application.dto.response.GoalResponse;
import com.backend.goal.domain.enums.GoalStatus;
import com.backend.goal.domain.enums.RewardType;
import com.backend.goal.domain.event.RemoveRelatedDetailGoalEvent;
import com.backend.goal.domain.repository.GoalListResponseDto;
import com.backend.goal.domain.repository.GoalQueryRepository;
import com.backend.goal.domain.repository.GoalRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private static final int RANDOM_GOAL_COUNT = 3;

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
    public GoalResponse updateGoal(final Long id, final GoalUpdateRequest goalSaveRequest) {

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(id);
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

    public List<GoalListResponseDto> getStoredGoalList() {

        List<Goal> storedGoalList = goalRepository.getGoalsByGoalStatusAndIsDeletedFalse(GoalStatus.STORE);

        Random random = new Random();

        List<Goal> randomStoredGoalList = new ArrayList<>();

        int goalCount = Math.min(storedGoalList.size(), RANDOM_GOAL_COUNT);

        while (randomStoredGoalList.size() < goalCount) {

            int index = random.nextInt(storedGoalList.size());
            Goal goal = storedGoalList.get(index);

            if (!randomStoredGoalList.contains(goal)) {
                randomStoredGoalList.add(goal);
            }
        }

        return randomStoredGoalList.stream().map(GoalListResponseDto::from).collect(Collectors.toList());
    }
}
