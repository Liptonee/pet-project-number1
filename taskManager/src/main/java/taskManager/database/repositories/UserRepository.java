package taskManager.database.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import taskManager.database.entities.UserEntity;




public interface  UserRepository extends JpaRepository<UserEntity, Long>{
    
    public Optional<UserEntity> findByUsername(String username);
    public Optional<UserEntity> findByEmail(String email);
}