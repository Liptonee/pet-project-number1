package taskManager.database;


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
    

    @OneToMany(mappedBy="user")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<UserTasksEntity> tasks = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<ProjectUsersEntity> projects = new ArrayList<>();


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
