package com.backend.retrospect.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    Optional<Retrospect> findByGoalId(Long goalId);
}
