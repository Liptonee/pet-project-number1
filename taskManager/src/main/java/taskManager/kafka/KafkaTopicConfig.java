package taskManager.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    //добавить в исполнители
    //добавить в проект
    //оставить коммент
    //скоро дедлайн
    @Bean
    public NewTopic commentCreatedTopic() {
        return TopicBuilder.name("comment.created")
                .partitions(3)
                .replicas(1)            
                .build();
    }

    @Bean
    public NewTopic executorAddedTopic(){
        return TopicBuilder.name("task.executor.added")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic memberAddedTopic(){
        return TopicBuilder.name("project.member.added")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deadlineApproachingTopic(){
        return TopicBuilder.name("task.deadline.approaching")
                .partitions(3)
                .replicas(1)
                .build();
    }

}   