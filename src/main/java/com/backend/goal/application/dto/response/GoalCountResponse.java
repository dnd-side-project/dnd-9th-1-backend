package com.backend.goal.application.dto.response;

import com.backend.goal.domain.enums.GoalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record GoalCountResponse(

        @Schema(implementation = Map.class, example = "{\"STORE\": \"1\", \"PROCESS\": \"2\", \"COMPLETE\": \"0\"}")
        Map<GoalStatus, Long> counts
) {
}
