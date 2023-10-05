package com.backend.auth.application;

import com.backend.auth.domain.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("사용자의 refresh token으로 UID를 반환한다.")
    void findUidByRefreshTokenTest(){
        // given
        RefreshToken refreshToken = new RefreshToken("uid", "token value", 10000L);

        // when
        refreshTokenService.saveRefreshToken(refreshToken.getUid(), refreshToken.getTokenValue(), refreshToken.getExpiration());
        String extractedUID = refreshTokenService.findUidByRefreshToken(refreshToken.getTokenValue());

        // then
        assertThat(extractedUID).isEqualTo(refreshToken.getUid());
    }
}