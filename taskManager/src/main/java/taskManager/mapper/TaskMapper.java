package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import taskManager.database.entity.TaskEntity;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    @Mapping(source="project.id",target="project_id")
    TaskResponse toResponse(TaskEntity entity);

    TaskEntity toEntity(Task dto);

}
