package com.backend.plan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    default Plan getById(Long id){

        return findById(id).orElseThrow(() -> new IllegalArgumentException());
    }
}
