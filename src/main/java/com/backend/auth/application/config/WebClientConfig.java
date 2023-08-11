package com.backend.auth.application.config;

import com.backend.auth.application.client.AppleClient;
import com.backend.auth.application.client.KakaoClient;
import com.backend.auth.application.client.OAuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(){
        return WebClient.create();
    }

    @Bean
    public OAuthClient kakaoClient(WebClient webClient){
        return new KakaoClient(webClient);
    }

    @Bean
    public OAuthClient appleClient(WebClient webClient){
        return new AppleClient(webClient);
    }
}
