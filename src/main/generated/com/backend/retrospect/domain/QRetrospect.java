package com.backend.retrospect.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRetrospect is a Querydsl query type for Retrospect
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRetrospect extends EntityPathBase<Retrospect> {

    private static final long serialVersionUID = 723065401L;

    public static final QRetrospect retrospect = new QRetrospect("retrospect");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final ListPath<RetrospectContent, QRetrospectContent> contents = this.<RetrospectContent, QRetrospectContent>createList("contents", RetrospectContent.class, QRetrospectContent.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> goalId = createNumber("goalId", Long.class);

    public final BooleanPath hasGuide = createBoolean("hasGuide");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<SuccessLevel> successLevel = createEnum("successLevel", SuccessLevel.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRetrospect(String variable) {
        super(Retrospect.class, forVariable(variable));
    }

    public QRetrospect(Path<? extends Retrospect> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRetrospect(PathMetadata metadata) {
        super(Retrospect.class, metadata);
    }

}

