package taskManager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entity.CommentEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.UserEntity;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-09T19:28:32+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponse toResponse(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long user_id = null;
        Long task_id = null;
        Long id = null;
        String message = null;

        user_id = entityUserId( entity );
        task_id = entityTaskId( entity );
        id = entity.getId();
        message = entity.getMessage();

        CommentResponse commentResponse = new CommentResponse( id, message, user_id, task_id );

        return commentResponse;
    }

    @Override
    public CommentEntity toEntity(Comment dto) {
        if ( dto == null ) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setMessage( dto.message() );

        return commentEntity;
    }

    private Long entityUserId(CommentEntity commentEntity) {
        UserEntity user = commentEntity.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long entityTaskId(CommentEntity commentEntity) {
        TaskEntity task = commentEntity.getTask();
        if ( task == null ) {
            return null;
        }
        return task.getId();
    }
}
