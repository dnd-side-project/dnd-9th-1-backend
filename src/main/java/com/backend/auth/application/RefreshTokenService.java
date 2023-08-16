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

    public  void saveRefreshToken(String refreshToken, String uid){
        refreshTokenRepository.save(new RefreshToken(refreshToken, uid)); 
    }
    public RefreshToken findUidByRefreshToken(String refreshToken){
        return refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
    }
}
