package com.backend.auth.presentation;

import com.backend.auth.application.OAuthService;
import com.backend.auth.presentation.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "소셜 로그인 API입니다.")
@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 후 사용자 토큰 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 실패"),
            @ApiResponse(responseCode = "401", description = "접근 권한 없음")
    })
    @PostMapping("/auth/{provider}")
    public ResponseEntity<LoginResponse> generateAccessToken(@PathVariable String provider, @RequestParam String userId) {
        return ResponseEntity.ok(oauthService.login(provider, userId));
    }

}
