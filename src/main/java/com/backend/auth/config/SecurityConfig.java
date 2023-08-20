package com.backend.auth.config;

import com.backend.auth.jwt.JwtAccessDeniedHandler;
import com.backend.auth.jwt.JwtAuthenticationEntryPoint;
import com.backend.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.MatcherType.mvc;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public static final String[] ENDPOINTS_WHITELIST = {
            "/**",
            "/swagger-ui/**",
            "/h2-console/**",
            "/auth/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/health");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 진입 지점 설정
                        .accessDeniedHandler(jwtAccessDeniedHandler)) // 접근 거부 핸들러 설정
                .headers(headers -> headers
                        .frameOptions((FrameOptionsConfig::disable))) // X-Frame-Options 설정 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 사용하지 않음
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 설정
                .apply(new JwtSecurityConfig(tokenProvider));
        return httpSecurity.build();
    }
}
