package com.backend.goal.domain.repository;

import com.backend.goal.domain.Goal;
import com.backend.goal.domain.enums.GoalStatus;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.backend.goal.domain.QGoal.*;

@Repository
@RequiredArgsConstructor
public class GoalQueryRepository {

    private final JPAQueryFactory query;

    public Slice<Goal> getGoalList(Long memberId, Long goalId, Pageable pageable, GoalStatus goalStatus)
    {
        List<Goal> goalList = query.select(goal)
                .from(goal)
                .where(
                        goal.memberId.eq(memberId),
                        goal.isDeleted.isFalse(), // 삭제되지 않은 상위 목표들만 선택
                        ltGoalId(goalId),
                        goal.goalStatus.eq(goalStatus)
                )
                .orderBy(goal.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        boolean hasNext = false;

        if (goalList.size() > pageable.getPageSize()) {
            hasNext = true;
            goalList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(goalList, pageable, hasNext);
    }

    public Long getGoalCountRetrospectEnabled(Long memberId)
    {
        return query.select(goal.count())
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(), // 삭제되지 않은 상위 목표들만 선택
                        goal.hasRetrospect.isFalse(), // 아직 회고를 작성하지 않는 것들 조회
                        goal.goalStatus.eq(GoalStatus.COMPLETE), // 완료상태인것들 체크
                        goal.memberId.eq(memberId)
                )
                .fetchOne();
    }

    public List<Goal> findGoalListReminderEnabled()
    {
        return query.select(goal)
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(), // 삭제되지 않은 상위 목표들만 선택
                        goal.goalStatus.eq(GoalStatus.PROCESS),
                        goal.reminderEnabled.isTrue()
                )
                .fetch();
    }

    public List<Goal> findGoalListEndDateExpired(LocalDate today)
    {
        return query.select(goal)
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(), // 삭제되지 않은 상위 목표들만 선택
                        goal.goalStatus.eq(GoalStatus.PROCESS),
                        goal.endDate.before(today)
                )
                .fetch();
    }


    public Map<GoalStatus, Long> getStatusCounts(Long memberId) {

        List<Tuple> counts = query
                .select(
                        goal.goalStatus,
                        goal.goalStatus.count()
                )
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(),
                        goal.memberId.eq(memberId)
                        )
                .groupBy(goal.goalStatus)
                .fetch();

        Map<GoalStatus, Long> statusCounts = new HashMap<>();

        for (Tuple tuple : counts) {

            statusCounts.put(tuple.get(goal.goalStatus), tuple.get(goal.goalStatus.count()));
        }

        // 진행상태별로 데이터가 없는 경우 0을 추가
        for (GoalStatus status : Arrays.asList(GoalStatus.PROCESS, GoalStatus.COMPLETE, GoalStatus.STORE)) {
            statusCounts.putIfAbsent(status, 0L);
        }

        return statusCounts;
    }

private BooleanExpression ltGoalId(Long goalId) {

        if (goalId == -1) {
            return null;
        }

        return goal.id.lt(goalId);
    }


}
