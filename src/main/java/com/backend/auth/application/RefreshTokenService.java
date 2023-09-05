package com.backend.auth.application;

import com.backend.auth.domain.RefreshToken;
import com.backend.auth.domain.RefreshTokenRepository;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public  void saveRefreshToken(String uid, String refreshToken){
        refreshTokenRepository.save(new RefreshToken(uid, refreshToken));
    }
    public String findUidByRefreshToken(String refreshToken){
        RefreshToken result = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return result.getUid();
    }

    public void deleteByUid(String uid) {
        refreshTokenRepository.findById(uid).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        refreshTokenRepository.deleteById(uid);
    }

}
