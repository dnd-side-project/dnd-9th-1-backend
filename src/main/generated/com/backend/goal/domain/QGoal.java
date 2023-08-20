package com.backend.goal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGoal is a Querydsl query type for Goal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoal extends EntityPathBase<Goal> {

    private static final long serialVersionUID = -1085434967L;

    public static final QGoal goal = new QGoal("goal");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final NumberPath<Integer> completedDetailGoalCnt = createNumber("completedDetailGoalCnt", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Integer> entireDetailGoalCnt = createNumber("entireDetailGoalCnt", Integer.class);

    public final EnumPath<GoalStatus> goalStatus = createEnum("goalStatus", GoalStatus.class);

    public final BooleanPath hasRetrospect = createBoolean("hasRetrospect");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final BooleanPath reminderEnabled = createBoolean("reminderEnabled");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QGoal(String variable) {
        super(Goal.class, forVariable(variable));
    }

    public QGoal(Path<? extends Goal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGoal(PathMetadata metadata) {
        super(Goal.class, metadata);
    }

}

