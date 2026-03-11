package taskManager.web.dto.response;

public record CommentResponse(
    Long id,
    String message,
    Long user_id,
    Long task_id
) {
    
}
