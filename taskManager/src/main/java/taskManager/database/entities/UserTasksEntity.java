package taskManager.database;



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
@Table(name = "user_tasks",
        uniqueConstraints = {
                @UniqueConstraint(
                    columnNames = {"user_id", "task_id"}
                )
            })
@Entity
public class UserTasksEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="task_id",nullable=false)
    private TaskEntity task;
    

}
