package com.backend.auth.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import org.springframework.data.repository.CrudRepository;

public interface FcmTokenRepository extends CrudRepository<FcmToken, String> {
    default FcmToken getById(String uid){
        return findById(uid).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
