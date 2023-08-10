package com.backend.auth.application.client;

import com.backend.user.domain.SocialType;

public class OAuthClientFactory {
    public static OAuthClient create(String provider){
        if(provider.equalsIgnoreCase(SocialType.KAKAO.toString())){
            return new KakaoClient();
        } else if(provider.equalsIgnoreCase(SocialType.APPLE.toString())){
            return new AppleClient();
        } else {
            throw new IllegalArgumentException("Invalid Provider : " + provider);
        }
    }
}
