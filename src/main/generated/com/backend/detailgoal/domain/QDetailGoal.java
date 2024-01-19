package com.backend.detailgoal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDetailGoal is a Querydsl query type for DetailGoal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDetailGoal extends EntityPathBase<DetailGoal> {

    private static final long serialVersionUID = -440401045L;

    public static final QDetailGoal detailGoal = new QDetailGoal("detailGoal");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final ListPath<java.time.DayOfWeek, EnumPath<java.time.DayOfWeek>> alarmDays = this.<java.time.DayOfWeek, EnumPath<java.time.DayOfWeek>>createList("alarmDays", java.time.DayOfWeek.class, EnumPath.class, PathInits.DIRECT2);

    public final BooleanPath alarmEnabled = createBoolean("alarmEnabled");

    public final TimePath<java.time.LocalTime> alarmTime = createTime("alarmTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> goalId = createNumber("goalId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDetailGoal(String variable) {
        super(DetailGoal.class, forVariable(variable));
    }

    public QDetailGoal(Path<? extends DetailGoal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDetailGoal(PathMetadata metadata) {
        super(DetailGoal.class, metadata);
    }

}

