package com.backend.auth.application;

import com.backend.auth.domain.BlackList;
import com.backend.auth.domain.BlackListRepository;
import com.backend.auth.jwt.exception.BlackListJwtException;
import com.backend.auth.jwt.exception.InvalidJwtException;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlackListService {
    private final BlackListRepository blackListRepository;

    public void saveBlackList(String accessToken, Long expiration){
        blackListRepository.save(new BlackList(accessToken, expiration));
    }

    public void checkBlackList(String accessToken){
        Optional<BlackList> blackList = blackListRepository.findByAccessToken(accessToken);
        if(blackList.isPresent()){
            throw new BlackListJwtException(ErrorCode.BLACK_LIST_TOKEN);
        }
    }

}
