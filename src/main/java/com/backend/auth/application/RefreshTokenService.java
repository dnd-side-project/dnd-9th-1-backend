package com.backend.auth.application;

import com.backend.auth.domain.RefreshToken;
import com.backend.auth.domain.RefreshTokenRepository;
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
                .orElseThrow(); // 예외 처리 (REFRESH_TOKEN_NOT_FOUND)
    }
}
