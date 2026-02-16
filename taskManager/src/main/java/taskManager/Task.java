package taskManager;

import java.time.LocalDate;

import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;


public record Task(
    Long id,
    String name,
    String description,
    TaskStatus status,
    LocalDate deadline,
    TaskPriority priority
) {
    
}
