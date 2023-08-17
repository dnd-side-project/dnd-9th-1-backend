package com.backend.auth.jwt;

import com.backend.auth.application.OAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TokenProviderTest {

    private final static String TEST_SECRET_KEY = "test_secret_key";

    @Autowired
    TokenProvider tokenProvider;

    @MockBean
    OAuthService oAuthService;

    String uid;

    @BeforeEach
    void setUp(){
        uid = "mock_social_uid";
    }

    @Test
    void generateAccessToken() {
        // when
        String accessToken = tokenProvider.generateAccessToken(uid);
        // then
        assertNotNull(accessToken);
    }

    @Test
    void generateRefreshToken() {
        // when
        String refreshToken = tokenProvider.generateRefreshToken(uid);
        // then
        assertNotNull(refreshToken);
    }

    @Test
    void getPayload() {
        // given
        String accessToken = tokenProvider.generateAccessToken(uid);
        // when
        String extractedUid = tokenProvider.getPayload(accessToken);
        // then
        assertEquals(uid, extractedUid);
    }

    @Test
    void validateToken() {
        // given
        String accessToken = tokenProvider.generateAccessToken(uid);
        // when & then
        Assertions.assertThatCode(() -> tokenProvider.validateToken(accessToken))
                        .doesNotThrowAnyException();
    }
}