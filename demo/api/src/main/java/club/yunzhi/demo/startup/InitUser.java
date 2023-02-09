package club.yunzhi.demo.startup;

import club.yunzhi.demo.entity.User;
import club.yunzhi.demo.properties.AppProperties;
import club.yunzhi.demo.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * 初始化测试账户
 * admin / admin
 */
@Component
public class InitUser implements ApplicationListener<ContextRefreshedEvent>, Ordered {
  public static int order = Integer.MIN_VALUE;

  private final AppProperties appProperties;
  private final UserRepository userRepository;

  public InitUser(AppProperties appProperties, UserRepository userRepository) {
    this.appProperties = appProperties;
    this.userRepository = userRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    if (userRepository.count() == 0) {
      User user = new User();
      user.setName(this.appProperties.getName());
      user.setUsername(this.appProperties.getUsername());
      user.setPassword(this.appProperties.getPassword());
      userRepository.save(user);
    }
    System.out.println(userRepository.count());
  }

  @Override
  public int getOrder() {
    return order;
  }
}
