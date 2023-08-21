package com.backend.auth.jwt;

import com.backend.auth.application.BlackListService;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    private final BlackListService blackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("url : " + request.getRequestURL());

        // 1. HttpServletRequest 헤더에서 토큰을 꺼낸다.
        String accessToken = tokenProvider.getToken(request.getHeader(AUTHORIZATION_HEADER));

        // 2. 블랙 리스트에 등록된 토큰인 경우 요청을 거부한다.
        blackListService.checkBlackList(accessToken);

        // 3. 토큰의 유효성을 검사한다. 정상 토큰인 경우, Authentication을 Security Context에 저장한다.
        tokenProvider.validateToken(accessToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. 필터를 거쳐 요청을 전달한다.
        filterChain.doFilter(request, response);
    }
}
