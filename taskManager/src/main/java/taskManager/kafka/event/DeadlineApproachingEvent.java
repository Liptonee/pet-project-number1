package taskManager.kafka.event;

import java.time.LocalDateTime;
import java.util.List;

public record  DeadlineApproachingEvent(
    List<String> recipientEmails,
    String projectName,
    String taskName,
    String priority,
    LocalDateTime deadline
) {

}
