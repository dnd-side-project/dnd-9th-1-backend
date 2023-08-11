package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OAuthClient {
    private static final String KAKAO_API_URL = "https://kapi.kakao.com/v2/user/me";

    private WebClient webClient;

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(KAKAO_API_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new Exception("잘못된 토큰 정보입니다.")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new Exception("내부 서버 오류")))
                .bodyToMono(OAuthUserInfo.class)
                .block();
    }
}
