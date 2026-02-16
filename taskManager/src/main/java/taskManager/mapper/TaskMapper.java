package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.Task;
import taskManager.database.entities.TaskEntity;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    Task toDto(TaskEntity entity);

    TaskEntity toEntity(Task dto);

}
