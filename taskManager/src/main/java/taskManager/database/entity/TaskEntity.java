package taskManager.database.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"commentsList", "executorsList"})
@Table(name = "tasks")
@Entity
public class TaskEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "name",nullable=false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable=false)
    private TaskStatus status;

    @Column(name = "deadline")
    private LocalDateTime deadline;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority",nullable=false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id", nullable=false) 
    private ProjectEntity project;



    @OneToMany(mappedBy="task", cascade = CascadeType.REMOVE)
    private List<CommentEntity> commentsList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="user_tasks",
                joinColumns=@JoinColumn(name="task_id",referencedColumnName="id"),
                inverseJoinColumns=@JoinColumn(name="user_id",referencedColumnName="id"),
                indexes = @Index(name = "idx_user_tasks_user_id", columnList = "user_id"))
    private List<UserEntity> executorsList = new ArrayList<>();


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
