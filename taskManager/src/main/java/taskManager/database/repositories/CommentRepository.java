package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import taskManager.database.entities.CommentEntity;

public interface  CommentRepository extends JpaRepository<CommentEntity, Long>{
    
}