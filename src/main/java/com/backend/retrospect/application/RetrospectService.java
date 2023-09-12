package com.backend.retrospect.application;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalRepository;
import com.backend.retrospect.application.dto.response.RetrospectResponse;
import com.backend.retrospect.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RetrospectService {

    private final GoalRepository goalRepository;

    private final RetrospectRepository retrospectRepository;

    public Long saveRetrospect(Long goalId, Boolean hasGuide, Map<Guide, String> contents, SuccessLevel successLevel) {
        findGoalAndCheckRetrospect(goalId);
        validateContentLength(hasGuide, contents);
        return retrospectRepository.save(new Retrospect(goalId, hasGuide, contents, successLevel)).getId();
    }

    public RetrospectResponse getRetrospect(Long goalId) {
            Retrospect retrospect = retrospectRepository.findByGoalId(goalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RETROSPECT_IS_NOT_WRITTEN));
        return RetrospectResponse.from(retrospect.getHasGuide(), retrospect.getContents(), retrospect.getSuccessLevel());
    }

    private void findGoalAndCheckRetrospect(Long goalId) {
        Goal goal = goalRepository.getByIdAndIsDeletedFalse(goalId);

        // 이미 회고를 작성한 상위 목표인 경우 회고를 작성할 수 없다.
        if(goal.getHasRetrospect()){
            throw new BusinessException(ErrorCode.ALREADY_HAS_RETROSPECT);
        }
        goal.writeRetrospect();
    }

    private void validateContentLength(Boolean hasGuide, Map<Guide, String> contents){
        if(hasGuide){ // 가이드 질문이 있는 경우에는 각 내용마다 최대 200자
            for(Map.Entry<Guide, String> entry: contents.entrySet()){
                String content = entry.getValue();
                if(content.length() > 200){
                    throw new BusinessException(ErrorCode.CONTENT_TOO_LONG);
                }
            }
        } else { // 가이드 질문이 없는 경우에는 하나의 내용이 최대 1000자
            String content = contents.entrySet().iterator().next().getValue();
            if(content.length() > 1000){
                throw new BusinessException(ErrorCode.CONTENT_TOO_LONG);
            }
        }
    }

}
