package taskManager.web.dto;

import java.time.LocalDateTime;

import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;

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
