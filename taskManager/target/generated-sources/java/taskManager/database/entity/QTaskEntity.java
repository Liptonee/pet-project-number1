package taskManager.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTaskEntity is a Querydsl query type for TaskEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaskEntity extends EntityPathBase<TaskEntity> {

    private static final long serialVersionUID = 1875833446L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTaskEntity taskEntity = new QTaskEntity("taskEntity");

    public final ListPath<CommentEntity, QCommentEntity> commentsList = this.<CommentEntity, QCommentEntity>createList("commentsList", CommentEntity.class, QCommentEntity.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deadline = createDateTime("deadline", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final ListPath<UserEntity, QUserEntity> executorsList = this.<UserEntity, QUserEntity>createList("executorsList", UserEntity.class, QUserEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<TaskPriority> priority = createEnum("priority", TaskPriority.class);

    public final QProjectEntity project;

    public final EnumPath<TaskStatus> status = createEnum("status", TaskStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QTaskEntity(String variable) {
        this(TaskEntity.class, forVariable(variable), INITS);
    }

    public QTaskEntity(Path<? extends TaskEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTaskEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTaskEntity(PathMetadata metadata, PathInits inits) {
        this(TaskEntity.class, metadata, inits);
    }

    public QTaskEntity(Class<? extends TaskEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new QProjectEntity(forProperty("project"), inits.get("project")) : null;
    }

}

