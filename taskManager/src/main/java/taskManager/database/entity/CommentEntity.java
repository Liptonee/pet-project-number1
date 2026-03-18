package taskManager.database.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Table(name = "comments", indexes = {
    @Index(name = "idx_comments_send_time", columnList = "created_at")})
@Entity
public class CommentEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "message",nullable=false)
    private String message;


    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName="id",nullable=false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="task_id",referencedColumnName="id",nullable=false)
    private TaskEntity task;
    
    
    @Column(name = "created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
