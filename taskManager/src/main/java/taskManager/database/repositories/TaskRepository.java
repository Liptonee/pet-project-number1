package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import taskManager.database.entities.TaskEntity;

public interface  TaskRepository extends JpaRepository<TaskEntity, Long>{
    
}