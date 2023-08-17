package com.backend.detailgoal.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.List;

public record DetailGoalUpdateRequest(


        @Size(max = 15, message = "상위 목표 제목은 15자를 초과할 수 없습니다.")
        String title,

        @NotNull(message = "알림 설정 여부는 빈값일 수 없습니다.")
        Boolean alarmEnabled,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime alarmTime,

        List<String> alarmDays
) {
}
