package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.user.domain.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OAuthHandler {
    private final List<OAuthClient> oAuthClientList;

    public OAuthHandler(List<OAuthClient> oAuthClientsList){
        this.oAuthClientList = oAuthClientsList;
    }

    public OAuthUserInfo getUserInfo(String accessToken, String provider) throws Exception {
        OAuthClient oAuthClient = getClient(provider);
        return oAuthClient.getUserInfo(accessToken);
    }

    private OAuthClient getClient(String provider) throws Exception {
        SocialType socialType = SocialType.valueOf(provider);
        return oAuthClientList.stream()
                .filter(c -> c.supports(socialType))
                .findFirst()
                .orElseThrow(Exception::new); // 커스텀 예외처리 "UnsupportedProviderException" 추가
    }
}
