package taskManager.kafka.event;

import java.util.List;



public record CommentCreatedEvent(
        List<String> recipientEmails,
        String taskName,
        String projectName,
        String authorName,
        String message
){

}
        