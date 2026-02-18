package taskManager.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CommentMapperImpl}.
 */
@Generated
public class CommentMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'commentMapperImpl'.
   */
  public static BeanDefinition getCommentMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CommentMapperImpl.class);
    InstanceSupplier<CommentMapperImpl> instanceSupplier = InstanceSupplier.using(CommentMapperImpl::new);
    instanceSupplier = instanceSupplier.andThen(CommentMapperImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
