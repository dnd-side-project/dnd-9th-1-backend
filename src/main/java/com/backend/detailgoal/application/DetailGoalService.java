package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.DetailGoalRepository;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.domain.GoalListResponseDto;
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

    List<DetailGoalResponse> getDetailGoalList(Long goalId)
    {
        List<DetailGoal> detailGoalList = detailGoalRepository.findDetailGoalsByGoalIdOrderByIdAsc(goalId);
        return detailGoalList.stream().map(DetailGoalResponse::from).collect(Collectors.toList());
    }


}
