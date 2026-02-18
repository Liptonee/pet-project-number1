package taskManager.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProjectMapperImpl}.
 */
@Generated
public class ProjectMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'projectMapperImpl'.
   */
  public static BeanDefinition getProjectMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProjectMapperImpl.class);
    InstanceSupplier<ProjectMapperImpl> instanceSupplier = InstanceSupplier.using(ProjectMapperImpl::new);
    instanceSupplier = instanceSupplier.andThen(ProjectMapperImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
