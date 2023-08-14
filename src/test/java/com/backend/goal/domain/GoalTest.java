package com.backend.goal.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class GoalTest {

    @DisplayName("상위 목표 종료 날짜가 시작날짜보다 빠르면 예외가 발생한다.")
    @Test
    void 상위목표_종료날짜가_시작날짜보다_빠르면_예외가_발생한다()
    {
        // given
        LocalDate startDate = LocalDate.of(2023,8,10);
        LocalDate endDate = LocalDate.of(2023,8,9);

        // when & then
        assertThatThrownBy(() -> {
            new Goal(1L, "테스트 제목", startDate, endDate, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 시작날짜가 최소 범위보다 작으면 예외가 발생한다.")
    @Test
    void 상위목표_시작날짜가_최소범위보다_작으면_예외가_발생한다()
    {
        // given
        LocalDate startDate = LocalDate.of(999,12,31);
        LocalDate endDate = LocalDate.of(2023,8,9);

        // when & then
        assertThatThrownBy(() -> {
            new Goal(1L, "테스트 제목", startDate, endDate, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 종료날짜가 최대 범위보다 크면 예외가 발생한다.")
    @Test
    void 상위목표_종료날짜가_최대범위보다_크면_예외가_발생한다()
    {
        // given
        LocalDate startDate = LocalDate.of(2023,8,10);
        LocalDate endDate = LocalDate.of(10000,1,1);

        // when & then
        assertThatThrownBy(() -> {
            new Goal(1L, "테스트 제목", startDate, endDate, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표의 제목이 15자를 초과하면 예외가 발생한다.")
    @Test
    void 상위목표의_제목길이가_15자를_초과하면_예외가_발생한다()
    {
        // given
        String title = "상위목표 제목 길이 체크하겠습니다";
        LocalDate startDate = LocalDate.of(2023,7,1);
        LocalDate endDate = LocalDate.of(2023,8,10);

        // when & then
        assertThatThrownBy(() -> {
            new Goal(1L, title, startDate, endDate, true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상위 목표 종료 날짜까지의 디데이를 구한다.")
    @Test
    void 상위목표의_종료날짜까지의_디데이를_구한다()
    {
        // given
        LocalDate startDate = LocalDate.of(2023,7,1);
        LocalDate endDate = LocalDate.of(2023,8,10);
        Goal goal = new Goal(1L, "테스트 제목", startDate, endDate, true);

        // when
        Long dDay = goal.calculateDday(LocalDate.of(2023, 7, 1));

        // then
        assertThat(dDay).isEqualTo(40L);
    }

    @DisplayName("현재 날짜가 상위 목표 날짜보다 뒤라면 디데이 구할때 예외가 발생한다.")
    @Test
    void 현재날짜가_상위목표날짜보다_뒤라면_디데이_연산시_예외가_발생한다()
    {
        // given
        LocalDate startDate = LocalDate.of(2023,7,1);
        LocalDate endDate = LocalDate.of(2023,8,10);
        Goal goal = new Goal(1L, "테스트 제목", startDate, endDate, true);

        // when & then
        assertThatThrownBy(() -> {
            goal.calculateDday(LocalDate.of(2023, 8, 12));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("현재 날짜와 종료 날짜가 같을때 디데이 0을 반환한다.")
    @Test
    void 현재날짜와_종료날짜가_같을때_디데이_0을_반환한다()
    {
        // given
        LocalDate startDate = LocalDate.of(2023,7,1);
        LocalDate endDate = LocalDate.of(2023,8,10);
        Goal goal = new Goal(1L, "테스트 제목", startDate, endDate, true);

        // when
        Long dDay = goal.calculateDday(LocalDate.of(2023, 8, 10));

        // then
        assertThat(dDay).isEqualTo(0L);
    }
}
