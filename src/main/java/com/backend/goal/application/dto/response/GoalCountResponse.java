package com.backend.goal.application.dto.response;

import com.backend.goal.domain.GoalStatus;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;

import java.util.HashMap;
import java.util.Map;

public record GoalCountResponse(

        @Schema(implementation = Map.class, example = "{\"STORE\": \"1\", \"PROCESS\": \"2\", \"COMPLETE\": \"0\"}")
        Map<GoalStatus, Long> counts
) {
}
