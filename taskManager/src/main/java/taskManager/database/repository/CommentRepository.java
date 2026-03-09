package taskManager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.CommentEntity;



@Repository
public interface  CommentRepository extends JpaRepository<CommentEntity, Long>,
                                            QuerydslPredicateExecutor<CommentEntity>{

    boolean existsByIdAndUserId(Long id, Long userId);

}