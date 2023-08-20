package com.backend.auth.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void insertAndGetTest() {
        // given
        RefreshToken refreshToken = new RefreshToken("refreshToken", "uid");

        // when
        refreshTokenRepository.save(refreshToken);
        RefreshToken extractedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken()).get();

        // then
        assertThat(extractedRefreshToken.getUid()).isEqualTo(refreshToken.getUid());
    }

}