package com.backend.detailgoal.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailGoalRepository extends JpaRepository<DetailGoal, Long> {

    List<DetailGoal> findDetailGoalsByGoalIdOrderByIdAsc(Long goalId);

    default DetailGoal getById(Long detailGoalId){

        return findById(detailGoalId).orElseThrow(() -> new BusinessException(ErrorCode.DETAIL_GOAL_NOT_FOUND));
    }
}
