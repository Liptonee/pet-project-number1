package taskManager.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ProjectMapperImpl}.
 */
@Generated
public class ProjectMapperImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ProjectMapperImpl apply(RegisteredBean registeredBean, ProjectMapperImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("userMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
