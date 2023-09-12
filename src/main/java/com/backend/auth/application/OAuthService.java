package com.backend.auth.application;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.response.LoginResponse;
import com.backend.auth.presentation.dto.response.ReissueResponse;
import com.backend.member.application.MemberService;
import com.backend.member.domain.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final BlackListService blackListService;

    private final FcmTokenService fcmTokenService;

    public LoginResponse login(String provider, String uid, String fcmToken) {
        Boolean isFirstLogin = memberService.findMemberOrRegister(Provider.from(provider), uid);

        String accessToken = tokenProvider.generateAccessToken(uid);
        String refreshToken = tokenProvider.generateRefreshToken(uid);

        log.info("save refresh token to redis : uid = {}, refresh token = {}", uid, refreshToken);
        refreshTokenService.saveRefreshToken(uid, refreshToken);

        boolean checkRefreshTokenSaved = refreshTokenService.checkRefreshTokenSaved(uid, refreshToken);
        log.info("check uid and refresh token saved : {}" , checkRefreshTokenSaved);

        fcmTokenService.saveFcmToken(uid, fcmToken);

        return new LoginResponse(isFirstLogin, accessToken, refreshToken);
    }

    public ReissueResponse reissue(String bearerRefreshToken) throws Exception {
        String refreshToken = tokenProvider.getToken(bearerRefreshToken);

        log.info("refresh token : " + refreshToken);
        String uid = refreshTokenService.findUidByRefreshToken(refreshToken);

        log.info("uid : " + uid);
        String renewAccessToken = tokenProvider.generateAccessToken(uid);
        String renewRefreshToken = tokenProvider.generateRefreshToken(uid);

        refreshTokenService.deleteByUid(uid);
        refreshTokenService.saveRefreshToken(uid, renewRefreshToken);

        return new ReissueResponse(renewAccessToken, renewRefreshToken);
    }

    public void logout(String bearerAccessToken)  {
        String accessToken = tokenProvider.getToken(bearerAccessToken);
        String uid = tokenProvider.getPayload(accessToken);

        refreshTokenService.deleteByUid(uid);
        fcmTokenService.deleteByUid(uid);

        Long expiration = tokenProvider.getExpiration(accessToken);
        blackListService.saveBlackList(accessToken, expiration);
    }

    public void withdraw(String bearerAccessToken)  {
        String accessToken = tokenProvider.getToken(bearerAccessToken);

        String uid = tokenProvider.getPayload(accessToken);
        memberService.withdraw(uid);
        refreshTokenService.deleteByUid(uid);
        fcmTokenService.deleteByUid(uid);

        Long expiration = tokenProvider.getExpiration(accessToken);
        blackListService.saveBlackList(accessToken, expiration);
    }

}
