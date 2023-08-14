package com.backend.auth.application;

import com.backend.auth.presentation.dto.response.LoginResponse;
import com.backend.global.util.JwtUtil;
import com.backend.member.application.MemberService;
import com.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {

    @Value("${jwt.secret}")
    private String key;

    private Long expireTime = 1000 * 60 * 60L;

    private final MemberService memberService;

    public LoginResponse login(String provider, String socialId) {
        Member member = memberService.findMemberOrRegister(provider, socialId);
        return new LoginResponse(JwtUtil.generateToken(member, key, expireTime));
    }
}
