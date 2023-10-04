package com.backend.global.config;

import com.backend.auth.application.BlackListService;
import com.backend.auth.jwt.filter.JwtExceptionFilter;
import com.backend.auth.jwt.handler.JwtAccessDeniedHandler;
import com.backend.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.backend.auth.jwt.filter.AuthenticationFilter;
import com.backend.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    private final BlackListService blackListService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/health", "/auth/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()

                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .headers()
                    .frameOptions().disable()
                .and()
                .authorizeHttpRequests()

                .anyRequest().authenticated()

                .and()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().disable()
                .addFilterBefore(new AuthenticationFilter(tokenProvider, blackListService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), AuthenticationFilter.class); // AuthenticationFilter에서 발생한 예외가 JwtExceptionFilter에서 처리된다.
        return httpSecurity.build();
    }
}