package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.Comment;
import taskManager.database.entities.CommentEntity;


@Mapper(componentModel = "spring",uses = {UserMapper.class,TaskMapper.class})
public interface CommentMapper {
    
    Comment toDto(CommentEntity entity);

    CommentEntity toEntity(Comment dto);
}
