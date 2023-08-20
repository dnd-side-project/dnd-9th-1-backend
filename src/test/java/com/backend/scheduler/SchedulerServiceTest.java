package com.backend.scheduler;

import com.backend.global.scheduler.SchedulerService;
import com.backend.goal.application.GoalService;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalQueryRepository;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.domain.GoalStatus;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SchedulerServiceTest {

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    GoalService goalService;


    @DisplayName("채움함 내의 종료 일자가 만료된 상위 목표는 보관함으로 이동한다.")
    @Test
    void 채움함내의_종료알자가_만료된_상위목표는_보관함으로_이동한다()
    {
        // given

        /*
        종료 기간이 만료된 채움함 내 상위 목표
         */
        for(int i =0; i < 3; i++)
        {
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.PROCESS);
            goalRepository.save(goal);
        }

        /*
        종료 기간이 만료되지 않은 채움함 내 상위 목표
         */
        for(int i =0; i < 3; i++)
        {
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(9999, 8, 1), true, GoalStatus.PROCESS);
            goalRepository.save(goal);
        }

        /*
        종료 기간이 만료됐지만 완료함에 있는 상위 목표
         */
        for(int i =0; i < 3; i++)
        {
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true, GoalStatus.COMPLETE);
            goalRepository.save(goal);
        }

        // when
        schedulerService.storeOutDateGoal();

        // then
        List<Goal> goalList = goalRepository.getGoalsByGoalStatusAndIsDeletedFalse(GoalStatus.STORE);
        assertThat(goalList).hasSize(3);
    }
}
