package taskManager.web.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import taskManager.database.entity.TaskPriority;

public record TaskPatch(
    
    @Size(max = 15, min = 2)
    String name,

    @Size(max = 1000)
    String description,

    @FutureOrPresent
    LocalDateTime deadline,
    
    TaskPriority priority
) {
    
}
