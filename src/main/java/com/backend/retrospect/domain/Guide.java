package com.backend.retrospect.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public enum Guide {

    LIKED("좋았던 점은 무엇인가요?"),
    LACKED("아쉬웠던 점이나, 부족했던 점은 무엇인가요?"),
    LEARNED("배운 점은 무엇인가요?"),
    LONGED_FOR("목표를 통해 뭘 얻고자 하셨나요?"),
    NONE("가이드 질문이 없음");

    @Getter
    private final String description;

    @JsonCreator
    public static Guide from(String guide){
        try {
            return Guide.valueOf(guide.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
