package taskManager.web.dto.request;

import jakarta.validation.constraints.NotNull;
import taskManager.database.entity.TaskStatus;

public record TaskStatusPatch(
    @NotNull
    TaskStatus status
) {
}
