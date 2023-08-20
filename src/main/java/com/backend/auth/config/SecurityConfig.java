package com.backend.auth.config;

import com.backend.auth.jwt.JwtAccessDeniedHandler;
import com.backend.auth.jwt.JwtAuthenticationEntryPoint;
import com.backend.auth.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/h2-console/**", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 진입 지점 설정
                        .accessDeniedHandler(jwtAccessDeniedHandler)) // 접근 거부 핸들러 설정
                .headers(headers -> headers
                        .frameOptions((HeadersConfigurer.FrameOptionsConfig::disable))) // X-Frame-Options 설정 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 사용하지 않음
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**", "/v2/api-docs/**").permitAll() // Swagger 인증 무시
                        .requestMatchers(
                                "/auth/**"
                        ).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 Database 인증 무시
                        .anyRequest().authenticated() // 이외의 요청은 인증 필요
                )
                .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 설정
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
