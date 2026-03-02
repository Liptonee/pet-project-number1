package taskManager.web.dto;

public record CommentResponse(
    Long id,
    String message,
    Long user_id,
    Long task_id
) {
    
}
