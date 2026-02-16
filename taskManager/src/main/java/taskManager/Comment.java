package taskManager;

import java.time.LocalDateTime;



public record Comment(
    Long id,
    Task task,
    String message,
    User user,
    LocalDateTime sendTime
) {
    
}
