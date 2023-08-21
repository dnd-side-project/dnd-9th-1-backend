package com.backend.auth.config;

import com.backend.auth.application.BlackListService;
import com.backend.auth.jwt.JwtAccessDeniedHandler;
import com.backend.auth.jwt.JwtAuthenticationEntryPoint;
import com.backend.auth.jwt.JwtFilter;
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
                .requestMatchers( "/**", "/swagger-ui/**", "/api-docs/**", "/health", "/auth/**");
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
                .requestMatchers("/**").permitAll()
                .and()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().disable()
                .addFilterBefore(new JwtFilter(tokenProvider, blackListService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}