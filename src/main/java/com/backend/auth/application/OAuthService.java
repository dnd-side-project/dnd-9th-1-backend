package com.backend.auth.application;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.response.LoginResponse;
import com.backend.member.application.MemberService;
import com.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    public LoginResponse login(String provider, String socialId) {
        Member member = memberService.findMemberOrRegister(provider, socialId);
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);
        return new LoginResponse(accessToken, refreshToken);
    }
}
