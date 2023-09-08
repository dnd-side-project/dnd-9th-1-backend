package com.backend.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto (

        @NotNull(message = "userId는 빈 값일 수 없습니다.")
        @Schema(example = "1234567890")
        String userId,

        @NotNull(message = "fcm token은 빈 값일 수 없습니다.")
        @Schema(example = "e3lMQkbQftspO12ei34bzp5xVu3wQp2R")
        String fcmToken
) { }