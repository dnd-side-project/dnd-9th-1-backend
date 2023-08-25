package com.backend.auth.jwt.exception;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidJwtException extends BusinessException {

    public InvalidJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
