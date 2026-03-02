package taskManager.database.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@ToString(exclude = {"tasksList", "membersList"})
@Table(name = "projects")
@Entity
public class ProjectEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @Column(name = "name",nullable=false)
    private String name;

    @Column(name = "description")
    private String description;

    
    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName="id",nullable=false)
    private UserEntity owner;


        @ManyToMany(mappedBy="participatedProjectsList")
        private List<UserEntity> membersList = new ArrayList<>();

    
    @OneToMany(mappedBy="project")
    private List<TaskEntity> tasksList = new ArrayList<>();


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
