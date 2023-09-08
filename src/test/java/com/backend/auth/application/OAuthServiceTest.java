package com.backend.auth.application;

import com.backend.auth.presentation.dto.response.LoginResponse;
import com.backend.auth.presentation.dto.response.ReissueResponse;
import com.backend.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "test")
public class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    private static String UID = "user1234";

    private static String BEARER_TOKEN_PREFIX = "Bearer ";

    private static String FCM_TOKEN = "fcm_token";

    @DisplayName("Access Token을 이용해 OAuth 인증 후 JWT를 발급한다.")
    @Test
    public void loginSuccess() {
        // given
        String provider = "kakao";

        //  when
        LoginResponse response = oAuthService.login(provider, UID, FCM_TOKEN);

        // then
        assertThat(response.accessToken()).isNotNull();
    }

    @DisplayName("kakao, apple 이외의 요청이 들어온 경우에 예외가 발생한다.")
    @Test
    @jakarta.transaction.Transactional
    public void loginFailedByInvalidProvider() {
        // given
        String provider = "naver";

        // when & then
        assertThrows(BusinessException.class,
                () -> oAuthService.login(provider, UID , FCM_TOKEN));
    }

    @DisplayName("access token이 만료되어 refresh token을 통해 재발급한다.")
    @Test
    @Transactional
    public void reissueRefreshToken() throws Exception {
        // given
        LoginResponse loginResponse = oAuthService.login("kakao", UID, FCM_TOKEN);

        // when
        ReissueResponse reissueResponse = oAuthService.reissue(BEARER_TOKEN_PREFIX + loginResponse.refreshToken());

        // then
        assertThat(reissueResponse.accessToken()).isNotNull();
        assertThat(loginResponse.refreshToken()).isNotNull();
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
    @Transactional
    public void logoutSuccess(){
        // given
        LoginResponse loginResponse = oAuthService.login("kakao", UID, FCM_TOKEN);

        // when & then
        assertDoesNotThrow(() -> oAuthService.withdraw( BEARER_TOKEN_PREFIX + loginResponse.accessToken()));
    }

}