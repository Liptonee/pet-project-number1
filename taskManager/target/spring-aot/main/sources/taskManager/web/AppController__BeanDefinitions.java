package taskManager.web;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AppController}.
 */
@Generated
public class AppController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'appController'.
   */
  private static BeanInstanceSupplier<AppController> getAppControllerInstanceSupplier() {
    return BeanInstanceSupplier.<AppController>forConstructor(AppService.class)
            .withGenerator((registeredBean, args) -> new AppController(args.get(0)));
  }

  /**
   * Get the bean definition for 'appController'.
   */
  public static BeanDefinition getAppControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AppController.class);
    beanDefinition.setInstanceSupplier(getAppControllerInstanceSupplier());
    return beanDefinition;
  }
}
