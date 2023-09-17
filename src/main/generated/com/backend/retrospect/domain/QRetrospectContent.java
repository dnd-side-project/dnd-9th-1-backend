package com.backend.retrospect.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRetrospectContent is a Querydsl query type for RetrospectContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRetrospectContent extends EntityPathBase<RetrospectContent> {

    private static final long serialVersionUID = 1919447040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRetrospectContent retrospectContent = new QRetrospectContent("retrospectContent");

    public final StringPath content = createString("content");

    public final EnumPath<Guide> guide = createEnum("guide", Guide.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRetrospect retrospect;

    public QRetrospectContent(String variable) {
        this(RetrospectContent.class, forVariable(variable), INITS);
    }

    public QRetrospectContent(Path<? extends RetrospectContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRetrospectContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRetrospectContent(PathMetadata metadata, PathInits inits) {
        this(RetrospectContent.class, metadata, inits);
    }

    public QRetrospectContent(Class<? extends RetrospectContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.retrospect = inits.isInitialized("retrospect") ? new QRetrospect(forProperty("retrospect")) : null;
    }

}

