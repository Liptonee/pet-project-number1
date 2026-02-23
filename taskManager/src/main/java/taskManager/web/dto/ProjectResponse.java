package taskManager.web.dto;

import java.time.LocalDateTime;

public record ProjectResponse(
    Long id,
    String name,
    String description,
    Long owner_id,
    LocalDateTime createdAt
) {
    
}
