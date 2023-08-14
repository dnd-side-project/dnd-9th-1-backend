package com.backend.auth.application;

import com.backend.auth.presentation.dto.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value="test")
public class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    @DisplayName("Access Token을 이용해 OAuth 인증 후 JWT를 발급한다.")
    @Test
    public void LoginSuccess() {
        // given
        String provider = "kakao";
        String userId = "abc123";

        //  when
        LoginResponse response = oAuthService.login(provider, userId);

        // then
        assertThat(response.accessToken()).isNotNull();
    }
}