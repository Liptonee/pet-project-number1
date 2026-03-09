package taskManager.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -1797039060L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final ListPath<CommentEntity, QCommentEntity> comments = this.<CommentEntity, QCommentEntity>createList("comments", CommentEntity.class, QCommentEntity.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ProjectEntity, QProjectEntity> ownProjectsList = this.<ProjectEntity, QProjectEntity>createList("ownProjectsList", ProjectEntity.class, QProjectEntity.class, PathInits.DIRECT2);

    public final ListPath<ProjectEntity, QProjectEntity> participatedProjectsList = this.<ProjectEntity, QProjectEntity>createList("participatedProjectsList", ProjectEntity.class, QProjectEntity.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final BooleanPath privateProfile = createBoolean("privateProfile");

    public final ListPath<TaskEntity, QTaskEntity> tasksList = this.<TaskEntity, QTaskEntity>createList("tasksList", TaskEntity.class, QTaskEntity.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath username = createString("username");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

