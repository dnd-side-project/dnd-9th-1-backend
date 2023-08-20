package com.backend.goal.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GoalRecoverRequest(

        @Schema(example = "2023-08-27", description = "현재 날짜보다 뒤의 날짜를 골라야 합니다")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate startDate,

        @Schema(example = "2023-08-28", description = "현재 날짜보다 뒤의 날짜를 골라야 합니다")
        @FutureOrPresent(message = "상위 목표 종료 일자는 과거 시점일 수 없습니다")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate,

        @Schema(description = "리마인드 설정 여부")
        @NotNull(message = "리마인드 알림 여부를 필수적으로 선택해야 합니다")
        Boolean reminderEnabled

) {
}
