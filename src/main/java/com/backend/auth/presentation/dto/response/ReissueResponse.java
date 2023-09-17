package com.backend.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReissueResponse(
    @Schema(description = "사용자 인증 후 발급한 access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    String accessToken,

    @Schema(description = "사용자 인증 후 발급한 refresh token", example = "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZ")
    String refreshToken
) {}
