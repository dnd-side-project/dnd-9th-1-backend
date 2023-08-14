package com.backend.auth.jwt;

import com.backend.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private final static String TEST_SECRET_KEY = "test_secret_key";

    @Autowired
    TokenProvider tokenProvider;

    Member mockMember;

    @BeforeEach
    void setUp(){
        mockMember = Member.from("apple", "mock_social_id");
    }

    @Test
    void generateAccessToken() {
        // when
        String accessToken = tokenProvider.generateAccessToken(mockMember);
        // then
        assertNotNull(accessToken);
    }

    @Test
    void generateRefreshToken() {
        // when
        String refreshToken = tokenProvider.generateRefreshToken(mockMember);
        // then
        assertNotNull(refreshToken);
    }

    @Test
    void getPayload() {
        // given
        String accessToken = "mock_access_token";
        // when
        String extractedSocialId = tokenProvider.getPayload(accessToken);
        // then
        assertEquals(mockMember.getSocialId(), extractedSocialId);
    }

    @Test
    void validateToken() {
        // given
        String accessToken = "mock_access_token";
        // when
        boolean isValidToken = tokenProvider.validateToken(accessToken);
        // then
        assertTrue(isValidToken);
    }
}