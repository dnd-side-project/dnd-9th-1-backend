package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AppleClient implements OAuthClient {

    private static final String APPLE_API_URL = "https://appleid.apple.com/auth/token";
    private WebClient webClient;

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(APPLE_API_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new Exception("잘못된 토큰 정보입니다.")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new Exception("내부 서버 오류")))
                .bodyToMono(OAuthUserInfo.class)
                .block();
    }
}
