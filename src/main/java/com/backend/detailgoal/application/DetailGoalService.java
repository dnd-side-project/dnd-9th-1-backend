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

    public List<DetailGoalListResponse> getDetailGoalList(Long goalId)
    {
        List<DetailGoal> detailGoalList = detailGoalRepository.findDetailGoalsByGoalIdAndIsDeletedFalse(goalId);
        return detailGoalList.stream().map(DetailGoalListResponse::from).collect(Collectors.toList());
    }

    public DetailGoalResponse getDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        return DetailGoalResponse.from(detailGoal);
    }

    @Transactional
    public void saveDetailGoal(Long goalId, DetailGoalSaveRequest detailGoalSaveRequest)
    {
        DetailGoal detailGoal = detailGoalSaveRequest.toEntity();
        detailGoal.setGoalId(goalId);
        detailGoalRepository.save(detailGoal);

        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        goal.increaseDetailGoalCnt();
    }

    @Transactional
    public GoalCompletedResponse removeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.remove();
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(detailGoal.getGoalId());
        goal.decreaseDetailGoalCnt();

        return new GoalCompletedResponse(checkEntireDetailGoalCompleted(detailGoal));
    }

    @Transactional
    public void updateDetailGoal(Long detailGoalId, DetailGoalUpdateRequest detailGoalUpdateRequest)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.update(detailGoalUpdateRequest.title(),
                          detailGoalUpdateRequest.alarmEnabled(),
                          detailGoalUpdateRequest.alarmTime(),
                          detailGoalUpdateRequest.alarmDays());
    }

    @Transactional
    public GoalCompletedResponse completeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.complete();
        return new GoalCompletedResponse(checkEntireDetailGoalCompleted(detailGoal));
    }



    @Transactional
    public void inCompleteDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(detailGoalId);
        detailGoal.inComplete();
    }

    private boolean checkEntireDetailGoalCompleted(DetailGoal detailGoal) {

        List<DetailGoal> detailGoalList = detailGoalRepository.findDetailGoalsByGoalIdAndIsDeletedFalse(detailGoal.getGoalId());
        int size = detailGoalList.size();
        long count = detailGoalList.stream().filter(DetailGoal::getIsCompleted).count();
        return size == (int) count;
    }

}
