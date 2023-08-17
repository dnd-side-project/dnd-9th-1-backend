package com.backend.auth.presentation.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
){}

