package com.backend.auth.application.client;

import com.backend.auth.application.dto.response.OAuthUserInfo;
import com.backend.user.domain.SocialType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class KakaoClient implements OAuthClient {
    private static final String KAKAO_BASE_URL = "https://kapi.kakao.com";
    private static final String KAKAO_URI = "/v2/user/me";
    private final WebClient kakaoOauthLoginClient;

    public KakaoClient(WebClient webClient){
        this.kakaoOauthLoginClient = kakaoOauthLoginClient(webClient);
    }

    @Override
    public boolean supports(SocialType provider) {
        return provider.isSameAs(SocialType.KAKAO);
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return kakaoOauthLoginClient.get()
                .uri(KAKAO_URI)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new Exception("Kakao Login: 잘못된 토큰 정보입니다.")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new Exception("Kakao Login: 내부 서버 오류")))
                .bodyToMono(OAuthUserInfo.class)
                .block();
    }

    private WebClient kakaoOauthLoginClient(WebClient webClient) {
        return webClient.mutate()
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
