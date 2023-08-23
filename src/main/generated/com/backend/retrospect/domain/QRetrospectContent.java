package com.backend.retrospect.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRetrospectContent is a Querydsl query type for RetrospectContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRetrospectContent extends EntityPathBase<RetrospectContent> {

    private static final long serialVersionUID = 1919447040L;

    public static final QRetrospectContent retrospectContent = new QRetrospectContent("retrospectContent");

    public final StringPath content = createString("content");

    public final EnumPath<Guide> guide = createEnum("guide", Guide.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QRetrospectContent(String variable) {
        super(RetrospectContent.class, forVariable(variable));
    }

    public QRetrospectContent(Path<? extends RetrospectContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRetrospectContent(PathMetadata metadata) {
        super(RetrospectContent.class, metadata);
    }

}

