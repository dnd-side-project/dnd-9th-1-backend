package com.backend.auth.presentation;

import com.backend.auth.application.OAuthService;
import com.backend.global.common.response.CustomResponse;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.*;

@Tag(name = "회원 인증", description = "소셜 로그인 API입니다.")
@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    @Operation(summary = "소셜 로그인",
                description = "카카오, 애플 서버에서 로그인한 사용자의 userId를 통해 access token과 refresh token을 반환합니다.")
    @PostMapping("/auth/{provider}")
    public ResponseEntity<CustomResponse> generateAccessTokenAndRefreshToken(
            @Parameter(description = "kakao, apple 중 현재 로그인하는 소셜 타입", in = ParameterIn.PATH) @PathVariable String provider,
            @Parameter(description = "사용자 ID") @RequestParam String userId) {
        return CustomResponse.success(LOGIN_SUCCESS, oauthService.login(provider, userId));
    }

    @Operation(summary = "토큰 재발급",
                description = "access token 만료 시 refresh token을 통해 access token을 재발급합니다.")
    @PostMapping("/reissue")
    @ExceptionHandler({UnsupportedJwtException.class, MalformedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<CustomResponse> reissue(@RequestHeader(value = "Authorization") String bearerRefreshToken) throws Exception {
        return CustomResponse.success(LOGIN_SUCCESS, oauthService.reissue(bearerRefreshToken));
    }

    @Operation(summary = "로그아웃", description = "사용자의 refresh token을 삭제하여 앱에서 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<CustomResponse> deleteRefreshToken(@RequestHeader(value = "Authorization") String bearerAccessToken) throws Exception {
        oauthService.logout(bearerAccessToken);
        return CustomResponse.success(LOGOUT_SUCCESS);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청 시 사용자의 상태를 DELETE로 변경한다.")
    public ResponseEntity<CustomResponse> withDraw(@RequestHeader(value = "Authorization") String bearerAccessToken) throws Exception {
        oauthService.withDraw(bearerAccessToken);
        return CustomResponse.success(DELETE_SUCCESS);
    }
}