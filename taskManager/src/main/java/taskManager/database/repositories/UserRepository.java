package taskManager.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import taskManager.database.entities.UserEntity;

public interface  UserRepository extends JpaRepository<UserEntity, Long>{
    
}