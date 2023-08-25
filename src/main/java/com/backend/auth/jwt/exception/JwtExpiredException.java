package com.backend.auth.jwt.exception;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import lombok.Getter;

@Getter
public class JwtExpiredException extends BusinessException {

    public JwtExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
