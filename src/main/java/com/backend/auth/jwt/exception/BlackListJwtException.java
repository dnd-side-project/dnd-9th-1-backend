package com.backend.auth.jwt.exception;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;

public class BlackListJwtException extends BusinessException {

    public BlackListJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
