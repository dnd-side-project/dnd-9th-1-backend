package com.backend.member.domain;

import java.util.Locale;

public enum Provider {
    KAKAO, APPLE;

    public static Provider from(String provider){
        return Provider.valueOf(provider.toUpperCase(Locale.ROOT));
    }
}