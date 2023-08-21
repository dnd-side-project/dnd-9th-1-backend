package com.backend.auth.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto (

        @NotNull(message = "userId는 빈 값일 수 없습니다.")
        String userId,

        @NotNull(message = "fcm token은 빈 값일 수 없습니다.")
        String fcmToken
) { }