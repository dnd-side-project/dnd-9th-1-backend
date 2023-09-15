package com.backend.goal.domain.repository;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GoalRepository extends JpaRepository<Goal, Long> {

    default Goal getByIdAndIsDeletedFalse(Long id){

        return findById(id).orElseThrow(() -> {throw new BusinessException(ErrorCode.GOAL_NOT_FOUND);
        });
    }

    int countByGoalStatusAndMemberIdAndIsDeletedFalse(GoalStatus goalStatus, Long memberId);

    List<Goal> getGoalsByGoalStatusAndMemberIdAndIsDeletedFalse(GoalStatus goalStatus, Long memberId);


}
