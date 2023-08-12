package com.backend.auth.presentation.dto.request;

public record LoginRequest(
        String accessToken,
        String provider
) { }
