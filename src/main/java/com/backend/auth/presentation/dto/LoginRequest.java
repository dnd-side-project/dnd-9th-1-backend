package com.backend.auth.presentation.dto;

public record LoginRequest(
        String accessToken,
        String provider
) { }
