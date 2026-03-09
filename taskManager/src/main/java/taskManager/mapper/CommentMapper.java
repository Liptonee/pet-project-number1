package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import taskManager.database.entity.CommentEntity;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;


@Mapper(componentModel = "spring",uses = {UserMapper.class,TaskMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    
    @Mapping(source="user.id",target="user_id")
    @Mapping(source="task.id",target="task_id")
    CommentResponse toResponse(CommentEntity entity);


    CommentEntity toEntity(Comment dto);
}
