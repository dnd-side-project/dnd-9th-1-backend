package com.backend.auth.presentation.dto;

public record LoginResponse (
        String accessToken,
        String nickname
){}

