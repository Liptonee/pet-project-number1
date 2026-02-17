package taskManager.mapper;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.Task;
import taskManager.database.entities.TaskEntity;
import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T20:04:26+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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

        taskEntity.setId( dto.id() );
        taskEntity.setName( dto.name() );
        taskEntity.setDescription( dto.description() );
        taskEntity.setStatus( dto.status() );
        taskEntity.setDeadline( dto.deadline() );
        taskEntity.setPriority( dto.priority() );

        return taskEntity;
    }
}
