package taskManager.kafka.event;

public record MemberAddedEvent(
        //пользователя добавляют в проект, ему приходить уведомление что он в таком то проекте
        String userEmail,
        String projectName
) {
}
