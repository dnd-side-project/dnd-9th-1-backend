package com.backend.member.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;

import java.util.Locale;

public enum Provider {
    KAKAO, APPLE;

    public static Provider from(String provider){
        try {
            return Provider.valueOf(provider.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}