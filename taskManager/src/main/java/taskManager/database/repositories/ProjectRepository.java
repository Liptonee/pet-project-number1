package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taskManager.database.entities.ProjectEntity;


@Repository
public interface  ProjectRepository extends JpaRepository<ProjectEntity, Long>{
    
}
