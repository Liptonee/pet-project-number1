package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taskManager.database.entities.CommentEntity;


@Repository
public interface  CommentRepository extends JpaRepository<CommentEntity, Long>{
    
}