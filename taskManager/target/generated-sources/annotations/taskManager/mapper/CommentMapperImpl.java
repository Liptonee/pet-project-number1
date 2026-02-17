package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taskManager.Comment;
import taskManager.Task;
import taskManager.User;
import taskManager.database.entities.CommentEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T20:04:26+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Comment toDto(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Task task = null;
        String message = null;
        User user = null;
        LocalDateTime sendTime = null;

        id = entity.getId();
        task = taskMapper.toDto( entity.getTask() );
        message = entity.getMessage();
        user = userMapper.toDto( entity.getUser() );
        sendTime = entity.getSendTime();

        Comment comment = new Comment( id, task, message, user, sendTime );

        return comment;
    }

    @Override
    public CommentEntity toEntity(Comment dto) {
        if ( dto == null ) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setId( dto.id() );
        commentEntity.setTask( taskMapper.toEntity( dto.task() ) );
        commentEntity.setMessage( dto.message() );
        commentEntity.setUser( userMapper.toEntity( dto.user() ) );
        commentEntity.setSendTime( dto.sendTime() );

        return commentEntity;
    }
}
