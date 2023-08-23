package com.backend.retrospect.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public enum SuccessLevel {

    LEVEL1("별로예요"),
    LEVEL2("아쉬워요"),
    LEVEL3("그저 그랬어요"), 
    LEVEL4("만족해요"), 
    LEVEL5("완전 만족해요");

    @Getter
    private final String description;

    @JsonCreator
    public static SuccessLevel from(String successLevel){
        try {
            return SuccessLevel.valueOf(successLevel.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
