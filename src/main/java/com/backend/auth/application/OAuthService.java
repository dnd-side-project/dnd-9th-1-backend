package com.backend.auth.application;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.response.AccessTokenResponse;
import com.backend.auth.presentation.dto.response.TokenResponse;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import com.backend.member.application.MemberService;
import com.backend.member.domain.Member;
import com.backend.member.domain.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    public TokenResponse login(String provider, String uid) {
        memberService.findMemberOrRegister(Provider.from(provider), uid);
        String accessToken = tokenProvider.generateAccessToken(uid);
        String refreshToken = tokenProvider.generateRefreshToken(uid);
        return new TokenResponse(accessToken, refreshToken);
    }

    public AccessTokenResponse reissue(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String accessToken = tokenProvider.reissueAccessToken(refreshToken);
        return new AccessTokenResponse(accessToken);
    }
}
