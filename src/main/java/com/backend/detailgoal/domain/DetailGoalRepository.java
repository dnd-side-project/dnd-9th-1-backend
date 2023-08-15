package com.backend.detailgoal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailGoalRepository extends JpaRepository<DetailGoal, Long> {

    List<DetailGoal> findDetailGoalsByGoalIdOrderByIdAsc(Long goalId);
}
