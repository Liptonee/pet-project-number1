package taskManager.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TaskMapperImpl}.
 */
@Generated
public class TaskMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'taskMapperImpl'.
   */
  public static BeanDefinition getTaskMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TaskMapperImpl.class);
    beanDefinition.setInstanceSupplier(TaskMapperImpl::new);
    return beanDefinition;
  }
}
