package taskManager.mapper;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entities.TaskEntity;
import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;
import taskManager.web.Task;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T20:46:14+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toDto(TaskEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        TaskStatus status = null;
        LocalDate deadline = null;
        TaskPriority priority = null;

        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        status = entity.getStatus();
        deadline = entity.getDeadline();
        priority = entity.getPriority();

        Task task = new Task( id, name, description, status, deadline, priority );

        return task;
    }

    @Override
    public TaskEntity toEntity(Task dto) {
        if ( dto == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setDeadline( dto.deadline() );
        taskEntity.setDescription( dto.description() );
        taskEntity.setId( dto.id() );
        taskEntity.setName( dto.name() );
        taskEntity.setPriority( dto.priority() );
        taskEntity.setStatus( dto.status() );

        return taskEntity;
    }
}
