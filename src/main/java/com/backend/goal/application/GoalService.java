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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;

    private final GoalQueryRepository goalQueryRepository;


    public Slice<GoalListResponse> getGoalList(Long goalId, Pageable pageable, GoalStatus goalStatus)
    {
        Slice<GoalListResponseDto> goalList = goalQueryRepository.getGoalList(goalId, pageable, goalStatus);
        return goalList.map(GoalListResponse::from);
    }

    public List<GoalCountResponse> getGoalCount()
    {
        List<GoalCountDto> goalCount = goalRepository.countGoalByGoalStatus();

        return goalCount.stream()
                .filter(goal -> goal.goalStatus().equals(GoalStatus.PROCESS) ||
                        goal.goalStatus().equals(GoalStatus.COMPLETE)
                ).map(goal -> new GoalCountResponse(goal.goalStatus(),goal.count()))
                .collect(Collectors.toList());
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
