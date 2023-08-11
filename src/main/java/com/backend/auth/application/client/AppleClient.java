package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;

public class AppleClient implements OAuthClient {
    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return null;
    }
}
