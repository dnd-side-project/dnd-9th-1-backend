package com.backend.goal.application.dto.response;

import com.backend.goal.domain.GoalStatus;

import java.util.Map;

public record GoalCountResponse(Map<GoalStatus, Long> counts) {
}
