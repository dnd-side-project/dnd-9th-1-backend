package com.backend.goal.application;

import com.backend.global.DatabaseCleaner;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.domain.GoalStatus;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("test")
public class GoalServiceTest {

    @Autowired
    DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
       databaseCleaner.execute();
    }

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

    @DisplayName("상위 목표 리스트를 처음 조회할때는 제일 최근 데이터부터 조회한다.")
    @Test
    void 상위목표_리스트를_처음_조회한다()
    {
        // given
        for(int i =0; i < 10; i++)
        {
            GoalSaveRequest placeSaveRequest = new GoalSaveRequest("제목 "+i, LocalDate.now(), LocalDate.now(), true);
            goalService.saveGoal(1L, placeSaveRequest);
        }

        // when
        Slice<GoalListResponse> goalList = goalService.getGoalList(null, Pageable.ofSize(5), GoalStatus.PROCESS);

        // then
        Assertions.assertThat(goalList.getContent()).hasSize(5);
        Assertions.assertThat(goalList.getContent().get(0).goalId()).isEqualTo(10L);
        Assertions.assertThat(goalList.hasNext()).isTrue();
    }

    @DisplayName("상위 목표 리스트를 조회할때 커서 값이 있다면 이후 값부터 조회한다.")
    @Test
    void 상위목표_리스트를_커서값_이후부터_조회한다()
    {
        // given
        for(int i =0; i < 10; i++)
        {
            GoalSaveRequest placeSaveRequest = new GoalSaveRequest("제목 "+i, LocalDate.now(), LocalDate.now(), true);
            goalService.saveGoal(1L, placeSaveRequest);
        }

        // when
        Slice<GoalListResponse> goalList = goalService.getGoalList(7L, Pageable.ofSize(5), GoalStatus.PROCESS);

        // then
        Assertions.assertThat(goalList.getContent()).hasSize(5);
        Assertions.assertThat(goalList.getContent().get(0).goalId()).isEqualTo(6L);
        Assertions.assertThat(goalList.hasNext()).isTrue();
    }

    @DisplayName("상위 목표 리스트를 조회할때 페이지 크기만큼 조회했지만 다음 데이터가 없다면 hasNext가 false를 반환한다")
    @Test
    void 상위목표_리스트의_페이지크기보다_데이터가_없다면_false를_반환한다()
    {
        // given
        for(int i =0; i < 10; i++)
        {
            GoalSaveRequest placeSaveRequest = new GoalSaveRequest("제목 "+i, LocalDate.now(), LocalDate.now(), true);
            goalService.saveGoal(1L, placeSaveRequest);
        }

        // when
        Slice<GoalListResponse> goalList = goalService.getGoalList(3L, Pageable.ofSize(5), GoalStatus.PROCESS);

        // then
        Assertions.assertThat(goalList.getContent()).hasSize(2);
        Assertions.assertThat(goalList.getContent().get(0).goalId()).isEqualTo(2L);
        Assertions.assertThat(goalList.hasNext()).isFalse();
    }

    @DisplayName("상위 목표 리스트를 조회할때 페이지 크기 보다 적은 데이터가 남았다면 hasNext가 false를 반환한다")
    @Test
    void 상위목표_리스트의_페이지크기만큼_조회하고_남은_데이터가_없다면_false를_반환한다()
    {
        // given
        for(int i =0; i < 10; i++)
        {
            GoalSaveRequest placeSaveRequest = new GoalSaveRequest("제목 "+i, LocalDate.now(), LocalDate.now(), true);
            goalService.saveGoal(1L, placeSaveRequest);
        }

        // when
        Slice<GoalListResponse> goalList = goalService.getGoalList(6L, Pageable.ofSize(5), GoalStatus.PROCESS);

        // then
        Assertions.assertThat(goalList.getContent()).hasSize(5);
        Assertions.assertThat(goalList.getContent().get(0).goalId()).isEqualTo(5L);
        Assertions.assertThat(goalList.hasNext()).isFalse();
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
