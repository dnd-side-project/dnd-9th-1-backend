package com.backend.detailgoal.application;

import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.application.dto.response.GoalCompletedResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.repository.DetailGoalRepository;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
import com.backend.detailgoal.presentation.dto.request.DetailGoalUpdateRequest;
import com.backend.global.DatabaseCleaner;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalRepository;
import com.backend.goal.domain.enums.GoalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DetailGoalServiceTest {

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    private DetailGoalRepository detailGoalRepository;

    @Autowired
    private DetailGoalService detailGoalService;

    @Autowired
    private GoalRepository goalRepository;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }



    @DisplayName("하위 목표를 생성하면 상위 목표의 하위 목표 카운트가 증가한다.")
    @Test
    void 하위목표를_생성하면_상위목표의_하위목표_카운트가_증가한다()
    {
         // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
        detailGoalService.saveDetailGoal(savedGoal.getId() ,detailGoalSaveRequest);

        // when
        List<DetailGoal> detailGoalList = detailGoalRepository.findAll();
        Goal findGoal = goalRepository.getByIdAndIsDeletedFalse(savedGoal.getId());

        // then
        assertThat(detailGoalList).hasSize(1);
        assertThat(findGoal.getEntireDetailGoalCnt()).isEqualTo(1);
    }


    @DisplayName("하위 목표를 수정한다.")
    @Test
    void 하위_목표를_수정한다()
    {
        // given
        DetailGoal detailGoal = new DetailGoal(1L, "테스트 제목", false, true, List.of(DayOfWeek.MONDAY,DayOfWeek.TUESDAY), LocalTime.of(10, 0));
        DetailGoal savedDetailGoal = detailGoalRepository.save(detailGoal);

        // when
        DetailGoalUpdateRequest detailGoalUpdateRequest = new DetailGoalUpdateRequest("수정된 제목", true, LocalTime.of(10, 0), List.of("TUESDAY", "FRIDAY"));
        detailGoalService.updateDetailGoal(savedDetailGoal.getId(), detailGoalUpdateRequest);

        // then
        DetailGoal updatedDetailGoal = detailGoalRepository.getByIdAndIsDeletedFalse(savedDetailGoal.getId());
        assertThat(updatedDetailGoal.getAlarmDays()).containsExactlyInAnyOrder(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY);
    }

    @DisplayName("하위 목표를 삭제하면 상위 목표의 하위 목표 카운트가 감소한다.")
    @Test
    void 하위목표를_삭제하면_상위목표의_하위목표_카운트가_감소한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
        detailGoalService.saveDetailGoal(savedGoal.getId() ,detailGoalSaveRequest);

        DetailGoalSaveRequest detailGoalSaveRequest2 = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
        detailGoalService.saveDetailGoal(savedGoal.getId() ,detailGoalSaveRequest2);

        detailGoalService.removeDetailGoal(savedGoal.getId());

        // when
        Goal findGoal = goalRepository.getByIdAndIsDeletedFalse(savedGoal.getId());

        // then
        assertThat(findGoal.getEntireDetailGoalCnt()).isEqualTo(1);
    }


    @DisplayName("하위 목표 상세 정보를 조회한다.")
    @Test
    void 하위_목표_상세정보를_조회한다()
    {
        // given
        DetailGoal detailGoal = new DetailGoal(1L, "테스트 제목", false, true, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), LocalTime.of(10, 0));
        DetailGoal savedDetailGoal = detailGoalRepository.save(detailGoal);

        // when
        DetailGoalResponse detailGoalResponse = detailGoalService.getDetailGoal(savedDetailGoal.getId());

        // then
        assertThat(detailGoalResponse.alarmDays()).usingRecursiveComparison().isEqualTo(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));
        assertThat(detailGoalResponse.alarmTime()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("하위 목표 리스트를 조회한다.")
    @Test
    void 하위_목표_리스트를_조회한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        for(int i = 0; i < 5; i++)
        {
            DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            detailGoalService.saveDetailGoal(savedGoal.getId() ,detailGoalSaveRequest);
        }

        // when
        List<DetailGoalListResponse> detailGoalList = detailGoalService.getDetailGoalList(savedGoal.getId());

        // then
        assertThat(detailGoalList).hasSize(5);
    }

    @DisplayName("하위 목표를 체크하면 완료 상태로 변한다")
    @Test
    void 하위목표를_체크하면_완료상태로_변한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoal detailGoal = new DetailGoal(savedGoal.getId(), "테스트 제목", false, true, List.of(DayOfWeek.MONDAY), LocalTime.of(10, 0));
        DetailGoal savedDetailGoal = detailGoalRepository.save(detailGoal);

        // when
        detailGoalService.completeDetailGoal(savedDetailGoal.getId());

        // then
        List<DetailGoal> result = detailGoalRepository.findAllByGoalIdAndIsDeletedFalse(savedDetailGoal.getGoalId());
        assertThat(result.get(0).getIsCompleted()).isTrue();
    }

    @DisplayName("하위 목표를 체크 해제하면 미완료 상태로 변한다")
    @Test
    void 하위목표를_체크해제_하면_미완료_상태로_변한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoal detailGoal = new DetailGoal(savedGoal.getId(), "테스트 제목", true, true, List.of(DayOfWeek.MONDAY), LocalTime.of(10, 0));
        DetailGoal savedDetailGoal = detailGoalRepository.save(detailGoal);

        // when
        detailGoalService.completeDetailGoal(savedDetailGoal.getId());
        detailGoalService.inCompleteDetailGoal(savedDetailGoal.getId());

        // then
        List<DetailGoal> result = detailGoalRepository.findAllByGoalIdAndIsDeletedFalse(savedDetailGoal.getGoalId());
        assertThat(result.get(0).getIsCompleted()).isFalse();
    }

    @DisplayName("완료 상태 하위 목표의 개수와 전체 하위 목표 개수가 같아지면 상위 목표가 성공한다")
    @Test
    void 완료_하위목표_개수와_전체_하위목표_개수가_같아지면_상위목표가_성공한다()
    {
        // given
        Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
        Goal savedGoal = goalRepository.save(goal);

        DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
        DetailGoal detailGoal = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest);

        DetailGoalSaveRequest detailGoalSaveRequest2 = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
        DetailGoal detailGoal1 = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest2);

        // when
        detailGoalService.completeDetailGoal(detailGoal.getId());
        GoalCompletedResponse goalCompletedResponse = detailGoalService.completeDetailGoal(detailGoal1.getId());

        // then
        assertThat(goalCompletedResponse.isGoalCompleted()).isTrue();
    }

    @Nested
    @DisplayName("하위 목표를 삭제했을때 하위목표 개수와 달성한하위목표 개수가 같으면")
    class 하위목표를_삭제했을때_하위목표개수와_달성한_하위목표_개수가_같으면{

        @DisplayName("상위 목표가 성공한다")
        @Test
        void 상위목표가_성공한다()
        {
            // given
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
            Goal savedGoal = goalRepository.save(goal);

            DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest);

            DetailGoalSaveRequest detailGoalSaveRequest2 = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal1 = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest2);

            // when
            GoalCompletedResponse beforeRemoveResponse = detailGoalService.completeDetailGoal(detailGoal.getId());
            GoalCompletedResponse afterRemovedResponse = detailGoalService.removeDetailGoal(detailGoal1.getId());

            // then
            assertThat(beforeRemoveResponse.isGoalCompleted()).isFalse();
            assertThat(afterRemovedResponse.isGoalCompleted()).isTrue();
        }

        @DisplayName("보석을 지급한다")
        @Test
        void 보석을_지급한다()
        {
            // given
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
            Goal savedGoal = goalRepository.save(goal);

            DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest);

            DetailGoalSaveRequest detailGoalSaveRequest2 = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal1 = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest2);

            // when
            detailGoalService.completeDetailGoal(detailGoal.getId());
            GoalCompletedResponse afterRemovedResponse = detailGoalService.removeDetailGoal(detailGoal1.getId());

            // then
            assertThat(afterRemovedResponse.rewardType()).isNotNull();
        }

        @DisplayName("지금까지 성공한 상위목표 개수를 반환한다")
        @Test
        void 지금까지_성공한_상위목표_개수를_반환한다()
        {
            // given
            Goal goal = new Goal(1L, "테스트 제목", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 24), true, GoalStatus.PROCESS);
            Goal savedGoal = goalRepository.save(goal);

            DetailGoalSaveRequest detailGoalSaveRequest = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest);

            DetailGoalSaveRequest detailGoalSaveRequest2 = new DetailGoalSaveRequest("테스트 제목", true, LocalTime.of(10, 0), List.of("MONDAY", "TUESDAY"));
            DetailGoal detailGoal1 = detailGoalService.saveDetailGoal(savedGoal.getId(), detailGoalSaveRequest2);

            // when
            detailGoalService.completeDetailGoal(detailGoal.getId());
            GoalCompletedResponse afterRemovedResponse = detailGoalService.removeDetailGoal(detailGoal1.getId());

            // then
            assertThat(afterRemovedResponse.completedGoalCount()).isEqualTo(1);
        }
    }

}
