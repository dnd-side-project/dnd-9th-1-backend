package com.backend.member.domain;

import java.util.Locale;

public enum SocialType {
    KAKAO, APPLE;

    public static SocialType from(String provider){
        return SocialType.valueOf(provider.toUpperCase(Locale.ROOT));
    }
}