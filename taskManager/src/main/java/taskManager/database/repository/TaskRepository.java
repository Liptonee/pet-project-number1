package taskManager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.TaskEntity;


@Repository
public interface  TaskRepository extends JpaRepository<TaskEntity, Long>,
                                        JpaSpecificationExecutor<TaskEntity>{
    


}