package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import taskManager.database.entity.TaskEntity;
import taskManager.web.dto.request.Task;
import taskManager.web.dto.response.TaskResponse;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    
    @Mapping(source="project.id",target="project_id")
    TaskResponse toResponse(TaskEntity entity);

    TaskEntity toEntity(Task dto);

}
