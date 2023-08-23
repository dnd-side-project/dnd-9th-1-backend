package com.backend.retrospect.application;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.retrospect.application.dto.response.RetrospectResponse;
import com.backend.retrospect.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RetrospectService {

    private final GoalRepository goalRepository;

    private final RetrospectRepository retrospectRepository;

    public Long saveRetrospect(Long goalId, Boolean hasGuide, Map<Guide, String> contents, SuccessLevel successLevel) {
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        if(goal.getHasRetrospect()){
            throw new BusinessException(ErrorCode.ALREADY_HAS_RETROSPECT);
        }
        goal.writeRetrospect();
        return retrospectRepository.save(new Retrospect(goalId, hasGuide, contents, successLevel)).getId();
    }

    public RetrospectResponse getRetrospect(Long goalId) {
        Retrospect retrospect = retrospectRepository.findByGoalId(goalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RETROSPECT_IS_NOT_WRITTEN));
        return RetrospectResponse.from(retrospect.getContents(), retrospect.getSuccessLevel());
    }
}
