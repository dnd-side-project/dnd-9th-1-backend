package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.application.dto.response.GoalCompletedResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.repository.DetailGoalRepository;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
import com.backend.detailgoal.presentation.dto.request.DetailGoalUpdateRequest;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalRepository;
import com.backend.goal.domain.enums.GoalStatus;
import com.backend.goal.domain.enums.RewardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailGoalService {

    private final DetailGoalRepository detailGoalRepository;

    private final GoalRepository goalRepository;

    private final RewardService rewardService;



    public List<DetailGoalListResponse> getDetailGoalList(Long goalId)
    {
        List<DetailGoal> detailGoalList = detailGoalRepository.findAllByGoalIdAndIsDeletedFalse(goalId);
        return detailGoalList.stream().map(DetailGoalListResponse::from).collect(Collectors.toList());
    }

    public DetailGoalResponse getDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        return DetailGoalResponse.from(detailGoal);
    }

    @Transactional
    public DetailGoal saveDetailGoal(Long goalId, DetailGoalSaveRequest detailGoalSaveRequest)
    {
        DetailGoal detailGoal = detailGoalSaveRequest.toEntity();
        detailGoal.setGoalId(goalId);
        DetailGoal savedDetailGoal = detailGoalRepository.save(detailGoal);

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        goal.increaseEntireDetailGoalCnt(); // 전체 하위 목표 개수 증가
        return savedDetailGoal;
    }

    @Transactional
    public GoalCompletedResponse removeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.remove();

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(detailGoal.getGoalId());
        goal.decreaseEntireDetailGoalCnt(); // 전체 하위 목표 감소

        if(detailGoal.getIsCompleted()) // 만약 이미 성취된 목표였다면, 성취된 목표 개수까지 함께 제거
        {
            goal.decreaseCompletedDetailGoalCnt();
        }

        boolean isCompleted = goal.checkGoalCompleted();

        if(isCompleted)
        {
            RewardType reward = rewardService.provideReward();
            goal.achieveReward(reward);
            goal.complete();

            int count = goalRepository.countByGoalStatusAndMemberIdAndIsDeletedFalse(GoalStatus.COMPLETE, goal.getMemberId());
            return new GoalCompletedResponse(isCompleted, goal.getReward(), count);
        }

        return new GoalCompletedResponse(isCompleted, goal.getReward(), 0);
    }

    @Transactional
    public Set<DayOfWeek> updateDetailGoal(Long detailGoalId, DetailGoalUpdateRequest detailGoalUpdateRequest)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        return detailGoal.update(detailGoalUpdateRequest.title(),
                detailGoalUpdateRequest.alarmEnabled(),
                detailGoalUpdateRequest.alarmTime(),
                detailGoalUpdateRequest.alarmDays());

    }

    @Transactional
    public GoalCompletedResponse completeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId); // 1. 삭제되지 않은 하위 목표 가져온다
        detailGoal.complete(); // 2. 하위 목표를 완료 상태로 변경한다.

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(detailGoal.getGoalId()); // 3. 전체 목표를 가져온다.
        goal.increaseCompletedDetailGoalCnt(); // 4. 완료한 하위 목표 개수를 증가시킨다.

        boolean isCompleted = goal.checkGoalCompleted(); // 5. 전체 하위 목표 개수와 완료한 하위 목표 개수가 같은지 체크한다.

        if(isCompleted)
        {
            RewardType reward = rewardService.provideReward(); // 6. 리워드를 랜덤으로 지급한다.
            goal.achieveReward(reward);
            goal.complete();

            int count = goalRepository.countByGoalStatusAndMemberIdAndIsDeletedFalse(GoalStatus.COMPLETE, goal.getMemberId());
            return new GoalCompletedResponse(isCompleted, goal.getReward(), count);
        }

        return new GoalCompletedResponse(isCompleted, goal.getReward(), 0);
    }


    @Transactional
    public void inCompleteDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.inComplete();
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(detailGoal.getGoalId());
        goal.decreaseCompletedDetailGoalCnt();
    }
}
