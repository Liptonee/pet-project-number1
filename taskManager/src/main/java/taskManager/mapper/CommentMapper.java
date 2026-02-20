package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.database.entities.CommentEntity;
import taskManager.web.dto.Comment;


@Mapper(componentModel = "spring",uses = {UserMapper.class,TaskMapper.class})
public interface CommentMapper {
    
    Comment toDto(CommentEntity entity);

    CommentEntity toEntity(Comment dto);
}
