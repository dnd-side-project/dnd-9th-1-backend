package com.backend.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExpireTimeRequest(
        @Schema(description = "만료 기한의 일수", example = "14")
        Long days,

        @Schema(description = "만료 기한의 시간", example = "0")
        Long hours,

        @Schema(description = "만료 기한의 분", example = "0")
        Long minutes,

        @Schema(description = "만료 기한의 초", example = "0")
        Long seconds
) {
    public Long toLong() {
        Long expireTime = 1000L;

        if(days!= 0) expireTime *= (days * 24 * 60 * 60);
        if(hours != 0) expireTime *= (hours * 60 * 60);
        if(minutes != 0) expireTime *= (minutes * 60);
        if(seconds != 0) expireTime *= seconds;
        return expireTime;
    }
}
