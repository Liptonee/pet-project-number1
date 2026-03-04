package taskManager.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import taskManager.database.entity.UserEntity;




@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
                                        JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}