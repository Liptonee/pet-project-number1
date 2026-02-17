package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.database.entities.TaskEntity;
import taskManager.web.Task;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    Task toDto(TaskEntity entity);

    TaskEntity toEntity(Task dto);

}
