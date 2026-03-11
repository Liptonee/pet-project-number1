package taskManager.web.dto.response;

import java.time.LocalDateTime;

public record ProjectResponse(
    Long id,
    String name,
    String description,
    Long owner_id,
    LocalDateTime createdAt
) {
    
}
