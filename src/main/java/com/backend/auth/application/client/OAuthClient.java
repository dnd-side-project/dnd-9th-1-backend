package com.backend.auth.application.client;

import com.backend.auth.application.dto.response.OAuthMemberInfo;
import com.backend.member.domain.SocialType;

public interface OAuthClient {
    boolean supports(SocialType provider);
    OAuthMemberInfo getMemberInfo(String accessToken);
}
