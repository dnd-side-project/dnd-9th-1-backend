package com.backend.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExpireTimeResponse(
        @Schema(description = "만료 기한의 일수", example = "14")
        Long days,

        @Schema(description = "만료 기한의 시간", example = "0")
        Long hours,

        @Schema(description = "만료 기한의 분", example = "0")
        Long minutes,

        @Schema(description = "만료 기한의 초", example = "0")
        Long seconds
) {
    public static ExpireTimeResponse from(Long expireTime) {
        expireTime /= 1000;

        Long days = expireTime / (24 * 60 * 60);
        if(days != 0) expireTime %= days;

        Long hours = expireTime / (60 * 60);
        if(hours != 0) expireTime %= hours;

        Long minutes = expireTime / 60;
        if(minutes != 0) expireTime %= minutes;

        Long seconds = expireTime / 60;
        return new ExpireTimeResponse(days, hours, minutes, seconds);
    }
}
