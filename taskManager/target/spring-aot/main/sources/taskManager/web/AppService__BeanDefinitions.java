package taskManager.web;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import taskManager.database.repositories.ProjectRepository;
import taskManager.database.repositories.TaskRepository;
import taskManager.database.repositories.UserRepository;
import taskManager.mapper.CommentMapper;
import taskManager.mapper.ProjectMapper;
import taskManager.mapper.TaskMapper;
import taskManager.mapper.UserMapper;

/**
 * Bean definitions for {@link AppService}.
 */
@Generated
public class AppService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'appService'.
   */
  private static BeanInstanceSupplier<AppService> getAppServiceInstanceSupplier() {
    return BeanInstanceSupplier.<AppService>forConstructor(UserMapper.class, ProjectMapper.class, TaskMapper.class, CommentMapper.class, UserRepository.class, ProjectRepository.class, TaskRepository.class)
            .withGenerator((registeredBean, args) -> new AppService(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5), args.get(6)));
  }

  /**
   * Get the bean definition for 'appService'.
   */
  public static BeanDefinition getAppServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AppService.class);
    beanDefinition.setInstanceSupplier(getAppServiceInstanceSupplier());
    return beanDefinition;
  }
}
