package com.backend.auth.application;

import com.backend.auth.application.client.OAuthHandler;
import com.backend.auth.application.dto.response.OAuthMemberInfo;
import com.backend.auth.presentation.dto.request.LoginRequest;
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
    private final OAuthHandler oAuthHandler;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        OAuthMemberInfo memberInfo= oAuthHandler.getMemberInfo(loginRequest.accessToken(), loginRequest.provider());

        Member uncheckedMember = Member.from(memberInfo, loginRequest.provider());
        Member member = memberService.findMemberOrRegister(uncheckedMember);

        return new LoginResponse(JwtUtil.generateToken(member, key, expireTime), member.getNickname());
    }
}
