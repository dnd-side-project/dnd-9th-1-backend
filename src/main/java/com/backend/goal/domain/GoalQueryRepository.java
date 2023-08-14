package com.backend.goal.domain;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.goal.domain.QGoal.*;

@Repository
@RequiredArgsConstructor
public class GoalQueryRepository {

    private final JPAQueryFactory query;

    public Slice<GoalListResponseDto> getGoalList(Long goalId, Pageable pageable, GoalStatus goalStatus)
    {
        List<GoalListResponseDto> goalList = query.select(Projections.constructor(GoalListResponseDto.class,
                        goal.id,
                        goal.title,
                        goal.startDate,
                        goal.endDate,
                        goal.entireDetailGoalCnt,
                        goal.completedDetailGoalCnt))
                .from(goal)
                .where(
                        goal.deleted.isFalse(), // 삭제 되지 않은 것들만 조회
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

    private BooleanExpression ltGoalId(Long goalId) {

        if (goalId == null) {
            return null;
        }

        return goal.id.lt(goalId);
    }


}
