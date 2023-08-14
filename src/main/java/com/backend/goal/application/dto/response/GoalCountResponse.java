package com.backend.goal.application.dto.response;


import com.backend.goal.domain.GoalStatus;

public record GoalCountResponse(GoalStatus goalStatus, Long count){
}
