package com.backend.plan.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class PlanTest {

    @DisplayName("상위 목표 종료 날짜가 시작날짜보다 빠르면 예외가 발생한다.")
    @Test
    void 상위목표_종료날짜가_시작날짜보다_빠르면_예외가_발생한다()
    {
        // given
        LocalDateTime startTimeDate = LocalDateTime.of(2023,8,10,0,0);
        LocalDateTime endTimeDate = LocalDateTime.of(2023,8,9,0,0);

        // when & then
        assertThatThrownBy(() -> {
            new Plan(1L, "테스트 제목", startTimeDate, endTimeDate, true, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 시작날짜가 최소 범위보다 작으면 예외가 발생한다.")
    @Test
    void 상위목표_시작날짜가_최소범위보다_작으면_예외가_발생한다()
    {
        // given
        LocalDateTime startTimeDate = LocalDateTime.of(999,12,31,23,59);
        LocalDateTime endTimeDate = LocalDateTime.of(2023,8,9,0,0);

        // when & then
        assertThatThrownBy(() -> {
            new Plan(1L, "테스트 제목", startTimeDate, endTimeDate, true, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 종료날짜가 최대 범위보다 크면 예외가 발생한다.")
    @Test
    void 상위목표_종료날짜가_최대범위보다_크면_예외가_발생한다()
    {
        // given
        LocalDateTime startTimeDate = LocalDateTime.of(2023,8,10,0,0);
        LocalDateTime endTimeDate = LocalDateTime.of(10000,1,1,0,0);

        // when & then
        assertThatThrownBy(() -> {
            new Plan(1L, "테스트 제목", startTimeDate, endTimeDate, true, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 제목이 15자를 초과하면 예외가 발생한다.")
    @Test
    void 상위목표의_제목길이가_15자를_초과하면_예외가_발생한다()
    {
        // given
        String title = "상위목표 제목 길이 체크하겠습니다";
        LocalDateTime startTimeDate = LocalDateTime.of(2023,7,1,0,0);
        LocalDateTime endTimeDate = LocalDateTime.of(2023,8,10,0,0);

        // when & then
        assertThatThrownBy(() -> {
            new Plan(1L, title, startTimeDate, endTimeDate, true, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
