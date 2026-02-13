package taskManager.database.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@Entity
public class CommentEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id",nullable=false)
    private TaskEntity task;

    @Column(name = "message",nullable=false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable=false)
    private UserEntity user;

    @Column(name = "send_time",nullable=false,updatable=false)
    private LocalDateTime sendTime;
    
}
