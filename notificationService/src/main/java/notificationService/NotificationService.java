package notificationService;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.kafka.event.CommentCreatedEvent;
import taskManager.kafka.event.DeadlineApproachingEvent;
import taskManager.kafka.event.ExecutorAddedEvent;
import taskManager.kafka.event.MemberAddedEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    // Время жизни ключа идемпотентности (7 дней)
    private static final Duration IDEMPOTENCY_TTL = Duration.ofDays(7);

    public void handleCommentCreated(CommentCreatedEvent event) {
        log.info("Processing CommentCreatedEvent: {}", event);

        String key = String.format("idemp::comment::%s::%s::%s",
                event.taskName(), event.projectName(), event.authorName());
        Boolean isNew = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", IDEMPOTENCY_TTL);
                
        if (Boolean.TRUE.equals(isNew)) {
            List<String> recipients = event.recipientEmails();
            if (recipients != null && !recipients.isEmpty()) {
                String subject = "New comment in task: " + event.taskName();
                for (String email : recipients) {
                    emailService.sendEventAsJson(email, subject, event);
                }
            } else {
                log.info("No recipients for comment event, skipping email");
            }
        } else {
            log.debug("Duplicate event, skipping: {}", key);
        }
    }

    public void handleExecutorAdded(ExecutorAddedEvent event) {
        log.info("Processing ExecutorAddedEvent: {}", event);
        String key = String.format("idemp::executor::%s::%s",
                event.taskName(), event.userEmail());
        Boolean isNew = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", IDEMPOTENCY_TTL);
        if (Boolean.TRUE.equals(isNew)) {
            String subject = "You have been assigned to task: " + event.taskName();
            emailService.sendEventAsJson(event.userEmail(), subject, event);
        } else {
            log.debug("Duplicate event, skipping: {}", key);
        }
    }

    public void handleMemberAdded(MemberAddedEvent event) {
        log.info("Processing MemberAddedEvent: {}", event);
        String key = String.format("idemp::member::%s::%s",
                event.projectName(), event.userEmail());
        Boolean isNew = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", IDEMPOTENCY_TTL);
        if (Boolean.TRUE.equals(isNew)) {
            String subject = "You have been added to project: " + event.projectName();
            emailService.sendEventAsJson(event.userEmail(), subject, event);
        } else {
            log.debug("Duplicate event, skipping: {}", key);
        }
    }

    public void handleDeadlineApproaching(DeadlineApproachingEvent event) {
        log.info("Processing DeadlineApproachingEvent: {}", event);
        String key = String.format("idemp::deadline::%s::%s::%s",
                event.projectName(), event.taskName(), event.deadline().toString());
        Boolean isNew = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", IDEMPOTENCY_TTL);
        if (Boolean.TRUE.equals(isNew)) {
            List<String> recipients = event.recipientEmails();
            if (recipients != null && !recipients.isEmpty()) {
                String subject = "Deadline approaching: " + event.taskName();
                for (String email : recipients) {
                    emailService.sendEventAsJson(email, subject, event);
                }
            } else {
                log.info("No recipients for deadline event, skipping email");
            }
        } else {
            log.debug("Duplicate event, skipping: {}", key);
        }
    }
}