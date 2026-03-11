package taskManager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.TaskEntity;


@Repository
public interface  TaskRepository extends JpaRepository<TaskEntity, Long>,
                                        QuerydslPredicateExecutor<TaskEntity>{
    
    @Modifying
    @Query(value = "INSERT INTO user_tasks (task_id,user_id) VALUES (:taskId,:userId)", nativeQuery=true)
    void addExecutor(Long taskId, Long userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END "+
            "FROM TaskEntity t JOIN t.executorsList e "+
            "WHERE t.id = :taskId AND e.id = :executorId")
    boolean existsByIdAndExecutorId(Long taskId, Long executorId);
    
    @Modifying
    @Query(value = "DELETE FROM user_tasks WHERE task_id = :taskId AND user_id = :userId", nativeQuery = true)
    void deleteExecutor(@Param("taskId") Long taskId, @Param("userId") Long userId);

}