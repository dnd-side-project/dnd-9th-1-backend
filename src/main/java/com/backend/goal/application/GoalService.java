package com.backend.goal.application;

import com.backend.goal.application.dto.response.GoalCountResponse;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.domain.*;
import com.backend.goal.application.dto.response.GoalResponse;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import lombok.RequiredArgsConstructor;
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


    public GoalListResponse getGoalList(Long goalId, Pageable pageable, GoalStatus goalStatus)
    {
        Slice<GoalListResponseDto> goalList = goalQueryRepository.getGoalList(goalId, pageable, goalStatus);
        List<GoalListResponseDto> content = goalList.getContent();
        Boolean next = goalList.hasNext();
        return new GoalListResponse(content, next);
    }

    public GoalCountResponse getGoalCounts()
    {
        Map<GoalStatus, Long> statusCounts = goalQueryRepository.getStatusCounts();
        return new GoalCountResponse(statusCounts);
    }


    @Transactional
    public Long saveGoal(final Long memberId, final GoalSaveRequest goalSaveRequest)
    {
        Goal goal = goalSaveRequest.toEntity(memberId);
        return goalRepository.save(goal).getId();
    }

    @Transactional
    public GoalResponse updateGoal(final GoalUpdateRequest goalSaveRequest) {

        Goal goal = goalRepository.getById(goalSaveRequest.goalId());
        goal.update(goalSaveRequest.title(),goalSaveRequest.startDate(),goalSaveRequest.endDate(),goalSaveRequest.reminderEnabled());
        return GoalResponse.from(goal, goal.calculateDday(LocalDate.now()));
    }

    @Transactional
    public void removeGoal(Long goalId)
    {
        Goal goal = goalRepository.getById(goalId);
        goal.remove();
    }
}
