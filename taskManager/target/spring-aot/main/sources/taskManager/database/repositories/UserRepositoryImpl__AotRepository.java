package taskManager.database.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import taskManager.database.entities.UserEntity;

/**
 * AOT generated JPA repository implementation for {@link UserRepository}.
 */
@Generated
public class UserRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UserRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByUsername(java.lang.String)}.
   */
  public UserEntity findByUsername(String username) {
    String queryString = "SELECT u FROM UserEntity u WHERE u.username = :username";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("username", username);

    return (UserEntity) convertOne(query.getSingleResultOrNull(), false, UserEntity.class);
  }
}
