package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taskManager.database.entities.CommentEntity;
import taskManager.database.entities.UserEntity;
import taskManager.web.dto.Comment;
import taskManager.web.dto.Task;
import taskManager.web.dto.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-23T21:06:15+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        user = userEntityToUser( entity.getUser() );
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
        commentEntity.setMessage( dto.message() );
        commentEntity.setUser( userMapper.toEntity( dto.user() ) );
        commentEntity.setTask( taskMapper.toEntity( dto.task() ) );
        commentEntity.setSendTime( dto.sendTime() );

        return commentEntity;
    }

    protected User userEntityToUser(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        String email = null;
        String password = null;
        String username = null;

        email = userEntity.getEmail();
        password = userEntity.getPassword();
        username = userEntity.getUsername();

        User user = new User( email, password, username );

        return user;
    }
}
