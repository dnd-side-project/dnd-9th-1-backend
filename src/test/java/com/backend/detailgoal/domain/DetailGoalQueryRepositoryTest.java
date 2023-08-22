package com.backend.detailgoal.domain;


import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.domain.GoalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DetailGoalQueryRepositoryTest {

    @Autowired
    private DetailGoalQueryRepository detailGoalQueryRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private DetailGoalRepository detailGoalRepository;

    @DisplayName("하위 목표 설정시 선택한 요일과 시간에 해당하는 하위 목표 리스트를 조회한다")
    @Test
    void 하위목표_설정시_선택한_요일과_시간에_해당하는_하위목표_리스트를_조회한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.STORE);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoal detailGoal = new DetailGoal(savedGoal.getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), LocalTime.of(10, 0, 0));
        detailGoalRepository.save(detailGoal);

        DetailGoal detailGoal2 = new DetailGoal(savedGoal.getId(), "테스트 제목", false, true, List.of(DayOfWeek.TUESDAY), LocalTime.of(10, 0, 0));
        detailGoalRepository.save(detailGoal2);

        DetailGoal detailGoal3 = new DetailGoal(savedGoal.getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.WEDNESDAY), LocalTime.of(10, 0, 0));
        detailGoalRepository.save(detailGoal3);

        DetailGoal detailGoal4 = new DetailGoal(savedGoal.getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY), LocalTime.of(10, 1, 0));
        detailGoalRepository.save(detailGoal4);

        // when
        List<DetailGoalAlarmResponse> results = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(DayOfWeek.MONDAY, LocalTime.of(10, 0, 0));

        // then
        assertThat(results).hasSize(2);
    }
}