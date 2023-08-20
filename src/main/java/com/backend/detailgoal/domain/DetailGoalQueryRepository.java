package com.backend.detailgoal.domain;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.goal.domain.QGoal;
import com.backend.member.domain.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import static com.backend.detailgoal.domain.QDetailGoal.*;
import static com.backend.goal.domain.QGoal.*;
import static com.backend.member.domain.QMember.*;


@Repository
@RequiredArgsConstructor
public class DetailGoalQueryRepository {

    private final JPAQueryFactory query;

    public List<DetailGoalAlarmResponse> getMemberIdListDetailGoalAlarmTimeArrived(DayOfWeek dayOfWeek)
    {
        return query.select(Projections.constructor(DetailGoalAlarmResponse.class, member.id, detailGoal.title))
                .from(detailGoal)
                .innerJoin(goal).on(goal.id.eq(detailGoal.goalId))
                .innerJoin(member).on(member.id.eq(goal.memberId))
                .where(
                        detailGoal.isDeleted.isFalse(), // 삭제 되지 않은 것들만 조회
                        detailGoal.isCompleted.isFalse(), // 아직 완료되지 않은 것들 조회
                        detailGoal.alarmEnabled.isTrue(), // 알람을 허용한 하위 댓글 조회
                        detailGoal.alarmDays.contains(dayOfWeek) // 해당 요일에 알림을 주기로 되있는 댓글 조회
                )
                .fetch();
    }
}
