package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.application.dto.response.GoalCompletedResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.DetailGoalRepository;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
import com.backend.detailgoal.presentation.dto.request.DetailGoalUpdateRequest;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.domain.RewardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
        detailGoalRepository.save(detailGoal);

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        goal.increaseEntireDetailGoalCnt(); // 전체 하위 목표 개수 증가
        return detailGoal;
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
            return new GoalCompletedResponse(isCompleted, goal.getReward(), goal.getReward().order);
        }

        return new GoalCompletedResponse(isCompleted,null, null);
    }

    @Transactional
    public DetailGoal updateDetailGoal(Long detailGoalId, DetailGoalUpdateRequest detailGoalUpdateRequest)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.update(detailGoalUpdateRequest.title(),
                          detailGoalUpdateRequest.alarmEnabled(),
                          detailGoalUpdateRequest.alarmTime(),
                          detailGoalUpdateRequest.alarmDays());
        return detailGoal;
    }

    @Transactional
    public GoalCompletedResponse completeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId); // 1. 삭제되지 않은 세부 목표 가져온다
        detailGoal.complete(); // 완료 처리

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(detailGoal.getGoalId()); // 2. 전체 목표를 가져옴
        goal.increaseCompletedDetailGoalCnt(); // 3. 성공 개수 증가시키기

        boolean isCompleted = goal.checkGoalCompleted(); // 4. 전체 목표 개수와 성공 목표 개수가 같은지 체크

        if(isCompleted) // 5. 만약 상위 목표를 성공했다면
        {
            RewardType reward = rewardService.provideReward(); // 6. 리워드 랜덤으로 지금
            goal.achieveReward(reward);
            return new GoalCompletedResponse(isCompleted, goal.getReward(), goal.getReward().order); // 7. 성공과 함께 리워드 정보
        }

        return new GoalCompletedResponse(isCompleted,null, null); // 8. 아니면 미완료 여부만 알려줌
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
