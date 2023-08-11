package com.backend.auth.application.client;

import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.user.domain.SocialType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AppleClient implements OAuthClient {

    private static final String APPLE_API_URL = "https://appleid.apple.com/auth/token";
    private final WebClient appleOauthLoginClient;

    public AppleClient (final WebClient webClient){
        this.appleOauthLoginClient = appleOauthLoginClient(webClient);
    }

    @Override
    public boolean supports(SocialType provider) {
        return provider.isSameAs(SocialType.APPLE);
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return appleOauthLoginClient.get()
                .uri(APPLE_API_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new Exception("Apple Login: 잘못된 토큰 정보입니다.")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new Exception("Apple Login: 내부 서버 오류")))
                .bodyToMono(OAuthUserInfo.class)
                .block();
    }

    private WebClient appleOauthLoginClient(WebClient webClient) {
        return webClient.mutate()
                .baseUrl(APPLE_API_URL)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
