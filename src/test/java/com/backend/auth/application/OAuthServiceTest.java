package com.backend.auth.application;

import com.backend.auth.application.client.OAuthHandler;
import com.backend.auth.application.dto.response.OAuthMemberInfo;
import com.backend.auth.presentation.dto.request.LoginRequest;
import com.backend.auth.presentation.dto.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    @MockBean
    private OAuthHandler oAuthHandler;

    @DisplayName("Access Token을 이용해 OAuth 인증 후 JWT를 발급한다.")
    @Test
    public void LoginSuccess() throws Exception {
        // given
        String mockToken = "mock_access_token_for_kakao";
        LoginRequest loginRequest = new LoginRequest(mockToken, "KAKAO");

        OAuthMemberInfo mockMemberInfo = new OAuthMemberInfo("id", "nickname");
        given(oAuthHandler.getMemberInfo(anyString(), anyString())).willReturn(mockMemberInfo);

        // when
        LoginResponse loginResponse = oAuthService.login(loginRequest);

        // then
        assertThat(loginResponse.accessToken()).isNotNull();
    }
}