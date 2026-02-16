package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import taskManager.database.entities.ProjectEntity;

public interface  ProjectRepository extends JpaRepository<ProjectEntity, Long>{
    
}
