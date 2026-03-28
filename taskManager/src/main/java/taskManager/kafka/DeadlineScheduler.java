package taskManager.kafka;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskStatus;
import taskManager.database.repository.TaskRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final TaskRepository taskRepository;
    private final KafkaProducerService kafkaProducerService;

    private static final Duration NOTIFY_BEFORE = Duration.ofDays(1);
    private static final Duration SCAN_WINDOW = Duration.ofMinutes(5);

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void checkDeadlines(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plus(NOTIFY_BEFORE).minus(SCAN_WINDOW);
        LocalDateTime end = now.plus(NOTIFY_BEFORE).plus(SCAN_WINDOW);

        List<TaskEntity> tasks = taskRepository.findAllTasksWithDeadlineInRange(start, end);

        for (TaskEntity task : tasks){
            if (!task.getStatus().equals(TaskStatus.DONE)){
            kafkaProducerService.sendDeadlineApproachingEvent(task);
            }
        }
    }
}

