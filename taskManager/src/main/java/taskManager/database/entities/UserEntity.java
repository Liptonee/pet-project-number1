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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@ToString(exclude = {"ownProjectsList", "participatedProjectsList"})
@Table(name = "users")
@Entity
public class UserEntity {
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="email",nullable=false, unique=true)
    private String email;

    @Column(name="password",nullable=false)
    private String password;

    @Column(name="username",nullable=false, unique=true)
    private String username;
    

    @ManyToMany(mappedBy="executorsList")
    private List<TaskEntity> tasksList = new ArrayList<>();


    @OneToMany(mappedBy="user")
    private List<CommentEntity> comments = new ArrayList<>();


    @ManyToMany
    @JoinTable(name="user_projects",
                joinColumns = @JoinColumn(name="user_id",referencedColumnName="id"),
                inverseJoinColumns= @JoinColumn(name="project_id",referencedColumnName="id")
    )
    private List<ProjectEntity> participatedProjectsList = new ArrayList<>();


    @OneToMany(mappedBy="owner")
    private List<ProjectEntity> ownProjectsList = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
