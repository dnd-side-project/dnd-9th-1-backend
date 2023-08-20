package com.backend.goal.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;



public interface GoalRepository extends JpaRepository<Goal, Long> {

    default Goal getByIdAndIsDeletedFalse(Long id){

        return findById(id).orElseThrow(() -> {throw new BusinessException(ErrorCode.GOAL_NOT_FOUND);
        });
    }

    int countByGoalStatusAndIsDeletedFalse(GoalStatus goalStatus);
}
