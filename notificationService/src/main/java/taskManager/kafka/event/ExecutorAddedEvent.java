package taskManager.kafka.event;

public record ExecutorAddedEvent(
        String userEmail,
        String taskName,
        String projectName
) {
}
