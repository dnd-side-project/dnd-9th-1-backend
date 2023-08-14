package com.backend.goal.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record GoalUpdateRequest(

        @NotNull(message = "상위 목표 ID가 빈값일 수 없습니다")
        Long goalId,

        @Size(max = 15, message = "상위 목표 제목은 15자를 초과할 수 없습니다")
        String title,

        @FutureOrPresent(message = "상위 목표 시작 일자는 과거 시점일 수 없습니다")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate startDate,

        @FutureOrPresent(message = "상위 목표 종료 일자는 과거 시점일 수 없습니다")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate,

        @NotNull(message = "리마인드 알림 여부를 필수적으로 선택해야 합니다")
        Boolean reminderEnabled

) {
}
