package taskManager.database.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_tasks",
        uniqueConstraints = {
                @UniqueConstraint(
                    columnNames = {"task_id", "project_id"}
                )
            })
@Entity
public class ProjectTasksEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="task_id",nullable=false)
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name="project_id",nullable=false)
    private ProjectEntity project;
    

}
