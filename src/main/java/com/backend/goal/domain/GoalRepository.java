package com.backend.goal.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    default Goal getById(Long id){

        return findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Query(value =
            "SELECT "+
                    " new com.backend.goal.domain.GoalCountDto(pl.goalStatus, COUNT(pl.id))" +
                    "FROM Goal pl " +
                    "WHERE pl.deleted = false " +
                    "GROUP BY pl.goalStatus"
    )
    List<GoalCountDto> countGoalByGoalStatus();


}