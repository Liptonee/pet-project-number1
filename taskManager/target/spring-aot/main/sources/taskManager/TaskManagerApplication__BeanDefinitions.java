package taskManager;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TaskManagerApplication}.
 */
@Generated
public class TaskManagerApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'taskManagerApplication'.
   */
  public static BeanDefinition getTaskManagerApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TaskManagerApplication.class);
    beanDefinition.setInstanceSupplier(TaskManagerApplication::new);
    return beanDefinition;
  }
}
