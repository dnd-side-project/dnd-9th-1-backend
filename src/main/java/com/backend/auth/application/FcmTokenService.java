package com.backend.auth.application;

import com.backend.auth.domain.FcmToken;
import com.backend.auth.domain.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    private static final String FCM_TOKEN_PREFIX = "fcm";

    public void saveFcmToken(String uid, String fcmToken){
        fcmTokenRepository.save(new FcmToken(FCM_TOKEN_PREFIX + uid, fcmToken));
    }

    public String findFcmToken(String uid) {
        return fcmTokenRepository.getById(FCM_TOKEN_PREFIX + uid).getFcmToken();
    }
}
