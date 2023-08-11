package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.user.domain.SocialType;

public interface OAuthClient {
    boolean supports(SocialType provider);
    OAuthUserInfo getUserInfo(String accessToken);
}
