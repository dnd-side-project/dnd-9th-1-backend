package com.backend.auth.application;

import com.backend.auth.domain.BlackList;
import com.backend.auth.domain.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlackListService {
    private final BlackListRepository blackListRepository;

    public void saveBlackList(String accessToken, Long expiration){
        blackListRepository.save(new BlackList(accessToken, "logout", expiration));
    }

    public boolean isBlackList(String accessToken){
        Optional<BlackList> blackList = blackListRepository.findById(accessToken);
        return blackList.isPresent();
    }

}
