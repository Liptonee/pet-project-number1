package notificationService.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.NotificationService;
import taskManager.kafka.event.CommentCreatedEvent;
import taskManager.kafka.event.DeadlineApproachingEvent;
import taskManager.kafka.event.ExecutorAddedEvent;
import taskManager.kafka.event.MemberAddedEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    //для имемпт
    //сделай RedisConfig, написать в пропертис, здесь в редис добавлять key + value=1


    private final NotificationService notificationService;

    @KafkaListener(topics = "comment.created")
    public void handleCommentCreated(CommentCreatedEvent event){
        log.info(" NotificationService received CommentCreatedEvent: {}", event);
        notificationService.handleCommentCreated(event);
    }

    @KafkaListener(topics = "task.executor.added")
    public void handleExecutorAdded(ExecutorAddedEvent event){
        log.info("NotificationService received ExecutorAddedEvent: {}", event);
        notificationService.handleExecutorAdded(event);
    }
    
    @KafkaListener(topics = "project.member.added")
    public void handleMemberAdded(MemberAddedEvent event){
        log.info("NotificationService received MemberAddedEvent: {}", event);
        notificationService.handleMemberAdded(event);
    }
    
    @KafkaListener(topics = "task.deadline.approaching")
    public void handleDeadlineApproaching(DeadlineApproachingEvent event){
        log.info("NotificationService received DeadlineApproachingEvent: {}", event);
        notificationService.handleDeadlineApproaching(event);
    }
}
