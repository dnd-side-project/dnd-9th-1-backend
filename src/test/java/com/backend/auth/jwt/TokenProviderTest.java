package com.backend.auth.jwt;

import com.backend.auth.application.OAuthService;
import com.backend.member.application.MemberService;
import com.backend.member.domain.Member;
import com.backend.member.domain.MemberRepository;
import com.backend.member.domain.Provider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
class TokenProviderTest {

    private final static String TEST_SECRET_KEY = "test_secret_key";

    @Autowired
    TokenProvider tokenProvider;

    @MockBean
    OAuthService oAuthService;

    @MockBean
    MemberRepository memberRepository;

    String uid;

    @BeforeEach
    void setUp(){
        uid = "mock_social_uid";
    }

    @Test
    void generateAccessToken() {
        // given
        Member member = Member.from(Provider.APPLE, uid);
        BDDMockito.given(memberRepository.getByUid(anyString())).willReturn(member);
        // when
        String accessToken = tokenProvider.generateAccessToken(uid);
        // then
        assertNotNull(accessToken);
    }

    @Test
    void generateRefreshToken() {
        // given
        Member member = Member.from(Provider.APPLE, uid);
        BDDMockito.given(memberRepository.getByUid(anyString())).willReturn(member);
        // when
        String refreshToken = tokenProvider.generateRefreshToken(uid);
        // then
        assertNotNull(refreshToken);
    }

    @Test
    void getPayload() {
        // given
        Member member = Member.from(Provider.APPLE, uid);
        BDDMockito.given(memberRepository.getByUid(anyString())).willReturn(member);
        String accessToken = tokenProvider.generateAccessToken(uid);
        // when
        String extractedUid = tokenProvider.getPayload(accessToken);
        // then
        assertEquals(uid, extractedUid);
    }

    @Test
    void validateToken() {
        // given
        Member member = Member.from(Provider.APPLE, uid);
        BDDMockito.given(memberRepository.getByUid(anyString())).willReturn(member);
        String accessToken = tokenProvider.generateAccessToken(uid);
        // when & then
        Assertions.assertThatCode(() -> tokenProvider.validateToken(accessToken))
                        .doesNotThrowAnyException();
    }
}