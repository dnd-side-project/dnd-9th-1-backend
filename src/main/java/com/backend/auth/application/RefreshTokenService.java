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
    public String findUidByRefreshToken(String refreshToken){
        RefreshToken result = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return result.getUid();
    }

    public void deleteByUid(String uid) {
        // uid에 해당하는 정보가 redis에 있는지 확인
        refreshTokenRepository.findById(uid).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        refreshTokenRepository.deleteById(uid);
    }
}
