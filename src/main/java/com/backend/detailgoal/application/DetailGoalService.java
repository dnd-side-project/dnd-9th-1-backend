package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.DetailGoalRepository;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
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

    public List<DetailGoalListResponse> getDetailGoalList(Long goalId)
    {
        List<DetailGoal> detailGoalList = detailGoalRepository.findDetailGoalsByGoalIdOrderByIdAsc(goalId);
        return detailGoalList.stream().map(DetailGoalListResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public void saveDetailGoal(Long goalId, DetailGoalSaveRequest detailGoalSaveRequest)
    {
        DetailGoal detailGoal = detailGoalSaveRequest.toEntity();
        detailGoal.setGoalId(goalId);
        detailGoalRepository.save(detailGoal);
    }
}
