package taskManager.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link CommentMapperImpl}.
 */
@Generated
public class CommentMapperImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static CommentMapperImpl apply(RegisteredBean registeredBean, CommentMapperImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("userMapper").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("taskMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
