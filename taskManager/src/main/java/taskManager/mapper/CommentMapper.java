package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.database.entities.CommentEntity;
import taskManager.web.Comment;


@Mapper(componentModel = "spring",uses = {UserMapper.class,TaskMapper.class})
public interface CommentMapper {
    
    Comment toDto(CommentEntity entity);

    CommentEntity toEntity(Comment dto);
}
