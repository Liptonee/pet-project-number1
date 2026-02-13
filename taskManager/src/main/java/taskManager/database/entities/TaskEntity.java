package taskManager.database;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@Entity
public class TaskEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name = "description",nullable=false)
    private String description;

    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "deadline")
    private LocalDate deadline;
    
    @Column(name = "priority")
    private TaskPriority priority;


    @OneToMany(mappedBy="task")
    private List<ProjectTasksEntity> projects = new ArrayList<>();

    @OneToMany(mappedBy="task")
    private List<UserTasksEntity> executors = new ArrayList<>();

    @OneToMany(mappedBy="task")
    private List<CommentEntity> comments = new ArrayList<>();


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
