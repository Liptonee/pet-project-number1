package taskManager.kafka;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.UserEntity;
import taskManager.kafka.event.CommentCreatedEvent;
import taskManager.kafka.event.DeadlineApproachingEvent;
import taskManager.kafka.event.ExecutorAddedEvent;
import taskManager.kafka.event.MemberAddedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendCommentCreatedEvent(String message, TaskEntity task, String username, Long currentUserId){
        List<String> recipientEmails = task.getExecutorsList().stream()
                                            .filter(executor -> !executor.getId().equals(currentUserId))
                                            .map(executor -> executor.getEmail())
                                            .toList();
        CommentCreatedEvent event = new CommentCreatedEvent(
                                        recipientEmails,
                                        task.getName(),
                                        task.getProject().getName(),
                                        username,
                                        message);
        kafkaTemplate.send("comment.created", task.getId().toString(), event);
        log.info("From KAFKA PRODUCER SERVICE sent to topic comment.created with key = {} and value = {}", task.getId().toString(), event);
    }


    public void sendDeadlineApproachingEvent(TaskEntity task){
        List<String> recipientEmails = task.getExecutorsList().stream()
                                            .map(user -> user.getEmail())
                                            .toList();
        DeadlineApproachingEvent event = new DeadlineApproachingEvent(
            recipientEmails,
            task.getProject().getName(),
            task.getName(),
            task.getPriority().toString(),
            task.getDeadline()
        );
        kafkaTemplate.send("task.deadline.approaching",event);
        log.info("From KAFKA PRODUCER SERVICE sent to topic task.deadline.approaching with value = {}", event);
    }

    public void sendExecutorAddedEvent(UserEntity user, TaskEntity task){
        ExecutorAddedEvent event = new ExecutorAddedEvent(user.getEmail(), task.getName(), task.getProject().getName());
        kafkaTemplate.send("task.executor.added", event);
        log.info("From KAFKA PRODUCER SERVICE sent to topic task.executor.added with value = {}", event);
    }

    public void sendMemberAddedEvent(String userEmail, String projectName){
        MemberAddedEvent event = new MemberAddedEvent(userEmail, projectName);
        kafkaTemplate.send("project.member.added", event);
        log.info("From KAFKA PRODUCER SERVICE sent to topic project.member.added with value = {}", event);
    }
}
