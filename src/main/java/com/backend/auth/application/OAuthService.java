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

    private final RefreshTokenService refreshTokenService;

    private final BlackListService blackListService;

    public TokenResponse login(String provider, String uid) {
        memberService.findMemberOrRegister(Provider.from(provider), uid);
        String accessToken = tokenProvider.generateAccessToken(uid);
        String refreshToken = tokenProvider.generateRefreshToken(uid);
        refreshTokenService.saveRefreshToken(refreshToken, uid);
        return new TokenResponse(accessToken, refreshToken);
    }

    public AccessTokenResponse reissue(String bearerRefreshToken) throws Exception {
        String refreshToken = tokenProvider.getToken(bearerRefreshToken);
        tokenProvider.validateToken(refreshToken);
        String uid = refreshTokenService.findUidByRefreshToken(refreshToken);
        String renewAccessToken = tokenProvider.generateAccessToken(uid);
        return new AccessTokenResponse(renewAccessToken);
    }

    public void logout(String bearerAccessToken) throws Exception {
        String accessToken = tokenProvider.getToken(bearerAccessToken);
        tokenProvider.validateToken(accessToken);

        String uid = tokenProvider.getPayload(accessToken);
        refreshTokenService.deleteByUid(uid);

        Long expiration = tokenProvider.getExpiration(accessToken);
        blackListService.saveBlackList(accessToken, expiration);
    }
}
