package com.backend.auth.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record TokenReissueRequest(
        @NotNull(message = "access token reissue 시 refresh token 입력은 필수입니다.")
        String refreshToken
) { }
