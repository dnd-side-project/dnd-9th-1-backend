package com.backend.auth.application;

import com.backend.auth.presentation.dto.response.TokenResponse;
import com.backend.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    private static String uid = "user1234";

    private static String BEARER_TOKEN_PREFIX = "Bearer ";


    @DisplayName("Access Token을 이용해 OAuth 인증 후 JWT를 발급한다.")
    @Test
    public void loginSuccess() {
        // given
        String provider = "kakao";

        //  when
        TokenResponse response = oAuthService.login(provider, uid);

        // then
        assertThat(response.accessToken()).isNotNull();
    }

    @DisplayName("kakao, apple 이외의 요청이 들어온 경우에 예외가 발생한다.")
    @Test
    public void loginFailedByInvalidProvider() {
        // given
        String provider = "naver";

        // when & then
        assertThrows(BusinessException.class,
                () -> oAuthService.login(provider, uid));
    }

    @DisplayName("access token이 만료되어 refresh token을 통해 재발급한다.")
    @Test
    public void reissueRefreshToken() throws Exception {
        // given
        TokenResponse tokenResponse = oAuthService.login("kakao", uid);

        // when
        TokenResponse renewTokenResponse = oAuthService.reissue(BEARER_TOKEN_PREFIX + tokenResponse.refreshToken());

        // then
        assertThat(renewTokenResponse.accessToken()).isNotNull();
        assertThat(renewTokenResponse.refreshToken()).isNotNull();
    }

    @DisplayName("저장되어 있지 않은 refresh token이 입력되면 예외가 발생한다. ")
    @Test
    public void refreshTokenNotFound() {
        // given
        String invalidRefreshToken = "mock_refresh_token";
        // when & then
        assertThrows(Exception.class, () -> oAuthService.reissue(BEARER_TOKEN_PREFIX + invalidRefreshToken));
    }

    @DisplayName("로그아웃을 성공적으로 완료한다.")
    @Test
    public void logoutSuccess(){
        // given
        TokenResponse tokenResponse = oAuthService.login("kakao", uid);
        // when & then
        assertThatNoException().isThrownBy(() -> oAuthService.withdraw( BEARER_TOKEN_PREFIX + tokenResponse.accessToken()));
    }

}