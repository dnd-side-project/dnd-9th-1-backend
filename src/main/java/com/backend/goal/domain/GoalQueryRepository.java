package com.backend.goal.domain;

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

    public Slice<Goal> getGoalList(Long goalId, Pageable pageable, GoalStatus goalStatus)
    {
        List<Goal> goalList = query.select(goal)
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(), // 삭제 되지 않은 것들만 조회
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

    public Long getGoalCountRetrospectEnabled()
    {
        return query.select(goal.count())
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(), // 삭제 되지 않은 것들만 조회
                        goal.hasRetrospect.isFalse(), // 아직 회고를 작성하지 않는 것들 조회
                        goal.goalStatus.eq(GoalStatus.COMPLETE) // 완료상태인것들 체크
                )
                .fetchOne();
    }

    public List<Goal> findGoalListEndDateExpired()
    {
        return query.select(goal)
                .from(goal)
                .where(
                        goal.isDeleted.isFalse(),
                        goal.goalStatus.eq(GoalStatus.PROCESS),
                        goal.endDate.before(LocalDate.now())
                )
                .fetch();
    }

    public Map<GoalStatus, Long> getStatusCounts() {


        List<Tuple> counts = query
                .select(
                        goal.goalStatus,
                        goal.goalStatus.count()
                )
                .from(goal)
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

        if (goalId == null) {
            return null;
        }

        return goal.id.lt(goalId);
    }


}
