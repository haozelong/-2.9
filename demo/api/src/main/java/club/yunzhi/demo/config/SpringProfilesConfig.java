package club.yunzhi.demo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaoqiang
 */
@ConfigurationProperties(prefix = "spring.profiles")
@Component
public class SpringProfilesConfig {
  private static String active;

  public static String getActive() {
    return SpringProfilesConfig.active;
  }

  public void setActive(String active) {
    SpringProfilesConfig.active = active;
  }
}
