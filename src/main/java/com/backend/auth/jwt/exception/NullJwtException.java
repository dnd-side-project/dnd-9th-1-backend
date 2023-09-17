package com.backend.auth.jwt.exception;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;

public class NullJwtException extends BusinessException {

    public NullJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
