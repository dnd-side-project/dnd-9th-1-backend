package com.backend.detailgoal.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

public record DetailGoalUpdateRequest(


        @Size(max = 15, message = "상위 목표 제목은 15자를 초과할 수 없습니다.")
        @Schema(description = "하위 목표 제목", example = "오픽 노잼 IH 영상 보기")
        String title,

        @NotNull(message = "알림 설정 여부는 빈값일 수 없습니다.")
        @Schema(description = "알람 수행 여부")
        Boolean alarmEnabled,



        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "a hh:mm", timezone = "Asia/Seoul", locale = "ko_KR")
        @Schema(description = "알람 받을 시각", example = "오후 11:30")
        LocalTime alarmTime,

        @Schema(description = "요일 정보", example = "[\"MONDAY\", \"TUSEDAY\", \"FRIDAY\"]")
        List<String> alarmDays
) {
}
