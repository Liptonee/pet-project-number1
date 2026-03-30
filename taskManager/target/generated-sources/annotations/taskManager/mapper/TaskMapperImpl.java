package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.web.dto.request.Task;
import taskManager.web.dto.response.TaskResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T21:03:22+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Eclipse Adoptium)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskResponse toResponse(TaskEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long project_id = null;
        Long id = null;
        String name = null;
        String description = null;
        TaskStatus status = null;
        TaskPriority priority = null;
        LocalDateTime deadline = null;

        project_id = entityProjectId( entity );
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        status = entity.getStatus();
        priority = entity.getPriority();
        deadline = entity.getDeadline();

        TaskResponse taskResponse = new TaskResponse( id, name, description, status, priority, deadline, project_id );

        return taskResponse;
    }

    @Override
    public TaskEntity toEntity(Task dto) {
        if ( dto == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setName( dto.name() );
        taskEntity.setDescription( dto.description() );
        taskEntity.setDeadline( dto.deadline() );
        taskEntity.setPriority( dto.priority() );

        return taskEntity;
    }

    private Long entityProjectId(TaskEntity taskEntity) {
        ProjectEntity project = taskEntity.getProject();
        if ( project == null ) {
            return null;
        }
        return project.getId();
    }
}
