package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;

public interface OAuthClient {
    OAuthUserInfo getUserInfo(String accessToken);
}
