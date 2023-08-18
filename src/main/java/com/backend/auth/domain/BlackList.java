package com.backend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@RedisHash(value = "blackList")
public class BlackList {
    @Id
    private String accessToken;

    private String status;

    @TimeToLive
    private Long expiration;
}
