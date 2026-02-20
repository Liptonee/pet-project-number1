package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taskManager.database.entities.TaskEntity;


@Repository
public interface  TaskRepository extends JpaRepository<TaskEntity, Long>{
    
}