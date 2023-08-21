package com.backend.auth.application;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.response.TokenResponse;
import com.backend.member.application.MemberService;
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
        refreshTokenService.saveRefreshToken(uid, refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(String bearerRefreshToken) throws Exception {
        String refreshToken = tokenProvider.getToken(bearerRefreshToken);

        String uid = refreshTokenService.findUidByRefreshToken(refreshToken);
        String renewAccessToken = tokenProvider.generateAccessToken(uid);
        String renewRefreshToken = tokenProvider.generateRefreshToken(uid);
        return new TokenResponse(renewAccessToken, renewRefreshToken);
    }

    public void logout(String bearerAccessToken) throws Exception {
        String accessToken = tokenProvider.getToken(bearerAccessToken);

        String uid = tokenProvider.getPayload(accessToken);
        refreshTokenService.deleteByUid(uid);

        Long expiration = tokenProvider.getExpiration(accessToken);
        blackListService.saveBlackList(accessToken, expiration);
    }

    public void withDraw(String bearerAccessToken) throws Exception {
        String accessToken = tokenProvider.getToken(bearerAccessToken);

        String uid = tokenProvider.getPayload(accessToken);
        memberService.withDraw(uid);

        refreshTokenService.deleteByUid(uid);
    }

}
