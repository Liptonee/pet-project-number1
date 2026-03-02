package taskManager.web.exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime timestamp
) {
}
