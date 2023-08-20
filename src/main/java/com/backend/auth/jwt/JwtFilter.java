package com.backend.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. HttpServletRequest 헤더에서 토큰을 꺼낸다.
        String accessToken = tokenProvider.getToken(request.getHeader(AUTHORIZATION_HEADER));

        // 2. 토큰의 유효성을 검사한다. 정상 토큰인 경우, Authentication을 Security Context에 저장한다.
        try {
            tokenProvider.validateToken(accessToken);
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 3. 필터를 거쳐 요청을 전달한다.
        filterChain.doFilter(request, response);
    }
}
