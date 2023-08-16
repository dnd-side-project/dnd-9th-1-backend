package com.backend.auth.application;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.response.AccessTokenResponse;
import com.backend.auth.presentation.dto.response.TokenResponse;
import com.backend.global.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private String refreshToken;

    private String uid;

    @BeforeEach
    void setUp() {
        uid = "abc123";
        refreshToken = tokenProvider.generateRefreshToken(uid);
        refreshTokenService.saveRefreshToken(refreshToken, uid);
    }

    @DisplayName("Access Token을 이용해 OAuth 인증 후 JWT를 발급한다.")
    @Test
    public void LoginSuccess() {
        // given
        String provider = "kakao";

        //  when
        TokenResponse response = oAuthService.login(provider, uid);

        // then
        assertThat(response.accessToken()).isNotNull();
    }

    @DisplayName("kakao, apple 이외의 요청이 들어온 경우에 예외가 발생한다.")
    @Test
    public void LoginFailedByInvalidProvider() {
        // given
        String provider = "naver";

        // when & then
        assertThrows(BusinessException.class,
                () -> oAuthService.login(provider, uid));
    }

    @DisplayName("access token이 만료되어 refresh token을 통해 재발급한다.")
    @Test
    public void reissueRefreshToken() {
        // when
        AccessTokenResponse accessTokenResponse = oAuthService.reissue(refreshToken);

        // then
        assertThat(accessTokenResponse.accessToken()).isNotNull();
    }

    @DisplayName("저장되어 있지 않은 refresh token이 입력되면 예외가 발생한다. ")
    @Test
    public void refreshTokenNotFound() {
        // given
        String invalidRefreshToken = "mock_refresh_token";
        // when & then
        assertThrows(BusinessException.class, () -> oAuthService.reissue(invalidRefreshToken));
    }
}