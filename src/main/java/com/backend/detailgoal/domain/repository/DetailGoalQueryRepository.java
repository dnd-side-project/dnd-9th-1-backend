package com.backend.detailgoal.domain.repository;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.goal.domain.QGoal;
import com.backend.goal.domain.enums.GoalStatus;
import com.backend.member.domain.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import static com.backend.detailgoal.domain.QDetailGoal.*;
import static com.backend.goal.domain.QGoal.*;
import static com.backend.member.domain.QMember.*;


@Repository
@RequiredArgsConstructor
public class DetailGoalQueryRepository {

    private final JPAQueryFactory query;

    public List<DetailGoalAlarmResponse> getMemberIdListDetailGoalAlarmTimeArrived(DayOfWeek dayOfWeek, LocalTime alarmTime)
    {
        return query.select(Projections.constructor(DetailGoalAlarmResponse.class, member.uid, detailGoal.title))
                .from(detailGoal)
                .leftJoin(goal).on(goal.id.eq(detailGoal.goalId))
                .leftJoin(member).on(member.id.eq(goal.memberId))
                .where(
                        goal.goalStatus.eq(GoalStatus.PROCESS), // 채움함에 있는 상위 목표만 알림을 보낸다
                        detailGoal.isDeleted.isFalse(), // 삭제 되지 않은 것들만 조회
                        detailGoal.isCompleted.isFalse(), // 아직 완료되지 않은 것들 조회
                        detailGoal.alarmEnabled.isTrue(), // 알람을 허용한 하위 댓글 조회
                        detailGoal.alarmDays.contains(dayOfWeek), // 해당 요일에 알림을 주기로 되있는 댓글 조회
                        detailGoal.alarmTime.eq(alarmTime)
                )
                .fetch();
    }

}
