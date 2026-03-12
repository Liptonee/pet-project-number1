package taskManager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.ProjectEntity;



@Repository
public interface  ProjectRepository extends JpaRepository<ProjectEntity, Long>,
                                            QuerydslPredicateExecutor<ProjectEntity>{
    boolean existsByNameAndOwnerId(String name, Long ownerId);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
       "FROM ProjectEntity p JOIN p.membersList m " +
       "WHERE p.id = :projectId AND m.id = :memberId")
    boolean existsByIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Modifying
    @Query(value = "INSERT INTO user_projects (user_id, project_id) VALUES (:userId, :projectId)", nativeQuery=true)
    void addMember(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_projects WHERE project_id = :projectId AND user_id = :userId", nativeQuery=true)
    void deleteMember(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_projects WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllMembers(@Param("projectId") Long projectId);
}
