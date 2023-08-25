package com.backend.auth.jwt.filter;

import com.backend.auth.application.BlackListService;
import com.backend.auth.jwt.TokenProvider;
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
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    private final BlackListService blackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = tokenProvider.getToken(request.getHeader(AUTHORIZATION_HEADER));

            // 토큰의 유효성을 검증
            tokenProvider.validateToken(accessToken);
            blackListService.checkBlackList(accessToken);

            // 인증 정보를 Security Context에 설정 후 다음 단계를 진행
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e)
        {
            System.out.println("에러 처리");
        }

        filterChain.doFilter(request, response);
    }
}
