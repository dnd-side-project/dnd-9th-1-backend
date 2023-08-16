package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
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
        List<DetailGoal> detailGoalList = detailGoalRepository.findDetailGoalsByGoalIdOrderByIdAsc(goalId);
        return detailGoalList.stream().map(DetailGoalListResponse::from).collect(Collectors.toList());
    }

    public DetailGoalResponse getDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getById(detailGoalId);
        return DetailGoalResponse.from(detailGoal);
    }

    @Transactional
    public void saveDetailGoal(Long goalId, DetailGoalSaveRequest detailGoalSaveRequest)
    {
        DetailGoal detailGoal = detailGoalSaveRequest.toEntity();
        detailGoal.setGoalId(goalId);
        detailGoalRepository.save(detailGoal);

        Goal goal = goalRepository.getById(goalId);
        goal.increaseDetailGoalCnt();
    }

    @Transactional
    public void removeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getById(detailGoalId);
        detailGoal.remove();
        Goal goal = goalRepository.getById(detailGoal.getGoalId());
        goal.decreaseDetailGoalCnt();
    }

    @Transactional
    public void updateDetailGoal(Long detailGoalId, DetailGoalUpdateRequest detailGoalUpdateRequest)
    {
        DetailGoal detailGoal = detailGoalRepository.getById(detailGoalId);
        detailGoal.update(detailGoalUpdateRequest.title(),
                          detailGoalUpdateRequest.alarmEnabled(),
                          detailGoalUpdateRequest.alarmTime(),
                          detailGoalUpdateRequest.alarmDays());
    }

    @Transactional
    public void completeDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getById(detailGoalId);
        detailGoal.complete();
    }

    @Transactional
    public void inCompleteDetailGoal(Long detailGoalId)
    {
        DetailGoal detailGoal = detailGoalRepository.getById(detailGoalId);
        detailGoal.inComplete();
    }

}
