package taskManager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.ProjectEntity;



@Repository
public interface  ProjectRepository extends JpaRepository<ProjectEntity, Long>,
                                            QuerydslPredicateExecutor<ProjectEntity>{
    boolean existsByOwnerIdAndName(Long ownerId, String name);

    boolean existsByOwnerIdAndId(Long ownerId, Long id);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
       "FROM ProjectEntity p JOIN p.membersList m " +
       "WHERE p.id = :projectId AND m.id = :memberId")
    boolean existsByIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
