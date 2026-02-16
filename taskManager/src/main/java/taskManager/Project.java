package taskManager;

import java.time.LocalDateTime;




public record Project(
    Long id,
    String name,
    String description,
    User owner,
    LocalDateTime createdAt
) {
    
}
