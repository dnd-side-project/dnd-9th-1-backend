package com.backend.auth.application;

import com.backend.auth.domain.RefreshToken;
import com.backend.auth.domain.RefreshTokenRepository;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String uid, String refreshToken, Long expiration){
        refreshTokenRepository.save(new RefreshToken(uid, refreshToken, expiration));
    }

    public String findUidByRefreshToken(String refreshToken){
        RefreshToken result = refreshTokenRepository.findByTokenValue(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
        return result.getUid();
    }

    public void deleteByUid(String uid) {
        refreshTokenRepository.findById(uid).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        refreshTokenRepository.deleteById(uid);
    }

}
