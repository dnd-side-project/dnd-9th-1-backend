package com.backend.auth.presentation;

import com.backend.auth.application.OAuthService;
import com.backend.auth.presentation.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "소셜 로그인 API입니다.")
@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    @Operation(summary = "소셜 로그인 API입니다.",
            description = "카카오, 애플 서버에서 로그인한 사용자의 userId를 통해 access token과 refresh token을 반환합니다.")
    @PostMapping("/auth/{provider}")
    public ResponseEntity<LoginResponse> generateAccessTokenAndRefreshToken(
            @Parameter(description = ",kakao apple 중 현재 로그인하는 소셜 타입", in = ParameterIn.PATH) @PathVariable String provider,
            @Parameter(description = "사용자 ID") @RequestParam String userId) {
        return ResponseEntity.ok(oauthService.login(provider, userId));
    }
}
