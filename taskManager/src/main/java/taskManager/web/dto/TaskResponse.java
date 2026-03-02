package taskManager.web.dto;

import java.time.LocalDateTime;

import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;

public record TaskResponse (
    Long id,
    String name,
    String description,
    TaskStatus status,
    TaskPriority priority,
    LocalDateTime deadline,
    Long project_id
) {
    
}
