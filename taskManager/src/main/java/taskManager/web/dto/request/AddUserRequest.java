package taskManager.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddUserRequest (
    @NotNull
    Long userId
) {
}
