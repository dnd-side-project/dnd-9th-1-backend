package com.backend.detailgoal.domain;


import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.detailgoal.domain.repository.DetailGoalQueryRepository;
import com.backend.detailgoal.domain.repository.DetailGoalRepository;
import com.backend.global.DatabaseCleaner;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalRepository;
import com.backend.goal.domain.enums.GoalStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("하위 목표 설정시 선택한 요일과 시간에 등록된 채움함 내 하위목표 리스트를 조회한다")
    @Test
    void 하위목표_설정시_선택한_요일과_시간에_해당하는_하위목표_리스트를_조회한다()
    {
        // given
        Goal goal_process = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.PROCESS);
        Goal goal_store = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.STORE);
        Goal goal_complete = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.COMPLETE);
        List<Goal> goalList = goalRepository.saveAll(List.of(goal_process, goal_store, goal_complete));

        DetailGoal detailGoal1 = new DetailGoal(goalList.get(0).getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), LocalTime.of(10, 0, 0));
        DetailGoal detailGoal2 = new DetailGoal(goalList.get(1).getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), LocalTime.of(10, 0, 0));
        DetailGoal detailGoal3 = new DetailGoal(goalList.get(2).getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), LocalTime.of(10, 0, 0));
        detailGoalRepository.saveAll(List.of(detailGoal1,detailGoal2,detailGoal3));

        // when
        List<DetailGoalAlarmResponse> results = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(DayOfWeek.MONDAY, LocalTime.of(10, 0, 0));

        // then
        assertThat(results).hasSize(1);
    }
}
