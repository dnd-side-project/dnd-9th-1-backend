package com.backend.retrospect.application;

import com.backend.global.exception.BusinessException;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalRepository;
import com.backend.goal.domain.GoalStatus;
import com.backend.retrospect.application.dto.response.RetrospectResponse;
import com.backend.retrospect.domain.Guide;
import com.backend.retrospect.domain.SuccessLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "test")
public class RetrospectServiceTest {

    @Autowired
    private RetrospectService retrospectService;

    @Autowired
    private GoalRepository goalRepository;

    @DisplayName("회고 저장에 성공한다.")
    @Test
    void saveRetrospect() {
        // given
        Goal goal = new Goal(1L, "상위 목표", LocalDate.of(2023, 7, 10), LocalDate.of(2023, 8, 15), true, GoalStatus.COMPLETE);
        Long goalId = goalRepository.save(goal).getId();

        Map<Guide, String> contents = new HashMap<>();
        contents.put(Guide.LIKED, "좋았던 점은 이것입니다.");
        contents.put(Guide.LACKED, "부족한 건 없습니다.");
        contents.put(Guide.LEARNED, "배운 것은 너무 많습니다.");
        contents.put(Guide.LONGED_FOR, "꿈은 없고요, 놀고 싶습니다.");

        // when
        Long retrospectId = retrospectService.saveRetrospect(goalId, Boolean.TRUE, contents, SuccessLevel.LEVEL5);

        // then
        Goal savedGoal = goalRepository.getByIdAndIsDeletedFalse(goalId);
        assertThat(retrospectId).isNotNull();
        assertThat(savedGoal.getHasRetrospect()).isEqualTo(Boolean.TRUE);
    }

    @DisplayName("회고 조회에 성공한다.")
    @Test
    void getRetrospect() {
        // given
        Goal goal = new Goal(1L, "상위 목표", LocalDate.of(2023, 7, 10), LocalDate.of(2023, 8, 15), true, GoalStatus.COMPLETE);
        Long goalId = goalRepository.save(goal).getId();

        Map<Guide, String> contents = new HashMap<>();
        contents.put(Guide.LIKED, "좋았던 점은 이것입니다.");
        contents.put(Guide.LACKED, "부족한 건 없습니다.");
        contents.put(Guide.LEARNED, "배운 것은 너무 많습니다.");
        contents.put(Guide.LONGED_FOR, "꿈은 없고요, 놀고 싶습니다.");

        retrospectService.saveRetrospect(1L, true, contents, SuccessLevel.LEVEL5);

        RetrospectResponse expectedResponse = new RetrospectResponse(contents, SuccessLevel.LEVEL5);

        // when
        RetrospectResponse retrospect = retrospectService.getRetrospect(goalId);

        // then
        assertThat(retrospect).isEqualTo(expectedResponse);
    }

    @DisplayName("회고 글 입력의 길이가 1000자인 경우에도 성공적으로 저장된다.")
    @Test
    void saveRetrospectWithOneThousandContent(){
        // given
        Goal goal = new Goal(1L, "상위 목표", LocalDate.of(2023, 7, 10), LocalDate.of(2023, 8, 15), true, GoalStatus.COMPLETE);
        Long goalId = goalRepository.save(goal).getId();

        char[] chars = new char[1000];
        Arrays.fill(chars, 'a');
        String content = new String(chars);

        Map<Guide, String> contents = new HashMap<>();
        contents.put(Guide.NONE, content);

        // when & then
        assertThatNoException().isThrownBy(() ->  retrospectService.saveRetrospect(goalId, false, contents, SuccessLevel.LEVEL1));
    }

    @DisplayName("상위 목표가 회고를 작성하지 않아 회고를 조회하는 것에 실패한다.")
    @Test
    void failToGetRetrospect(){
        // given
        Goal goal = new Goal(1L, "상위 목표", LocalDate.of(2023, 7, 10), LocalDate.of(2023, 8, 15), true, GoalStatus.COMPLETE);
        Long goalId = goalRepository.save(goal).getId();

        // when & then
        assertThrows(BusinessException.class,
                () -> retrospectService.getRetrospect(goalId));
    }
}