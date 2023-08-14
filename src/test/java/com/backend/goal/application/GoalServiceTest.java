package com.backend.goal.application;

import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("test")
public class GoalServiceTest {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalService goalService;


    @DisplayName("상위 목표를 저장할 수 있다.")
    @Test
    void 상위목표를_저장할수_있다()
    {
        // given
        GoalSaveRequest placeSaveRequest = new GoalSaveRequest("제목", LocalDate.now(), LocalDate.now(), true);
        Long goalId = goalService.saveGoal(1L, placeSaveRequest);


        // then
        Goal savedUser = goalRepository.getById(goalId);
        Assertions.assertThat(savedUser.getTitle()).isEqualTo(placeSaveRequest.title());
    }

    @DisplayName("상위 목표를 수정할 수 있다.")
    @Test
    void 상위목표를_수정할수_있다()
    {
        // given

        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true);
        Goal savedGoal = goalRepository.save(goal);

        GoalUpdateRequest goalUpdateRequest = new GoalUpdateRequest(savedGoal.getId(), "수정된 제목", LocalDate.now(), LocalDate.now(), false);
        // when
        goalService.updateGoal(goalUpdateRequest);

        // then
        Goal updatedGoal = goalRepository.getById(savedGoal.getId());
        Assertions.assertThat(updatedGoal.getTitle()).isEqualTo(goalUpdateRequest.title());
    }

    @DisplayName("상위 목표를 삭제할 수 있다.")
    @Test
    void 상위목표를_삭제할수_있다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 1), true);
        Goal savedGoal = goalRepository.save(goal);

        // when
        goalService.removeGoal(savedGoal.getId());

        // then
        Goal removedGoal = goalRepository.getById(savedGoal.getId());
        Assertions.assertThat(removedGoal.getDeleted()).isTrue();
    }
}
