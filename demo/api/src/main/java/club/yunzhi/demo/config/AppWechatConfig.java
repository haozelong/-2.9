package club.yunzhi.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author panjie 3792535@qq.com
 * @date 2022/12/14
 * @blog https://segmentfault.com/u/myskies
 * @description
 */
@ConfigurationProperties(prefix = "app.wechat")
@Component
public class AppWechatConfig {
  /**
   * 微信主动发送消息模板
   */
  private Map<String, String> template;
  /**
   * 微信消息发送的最小间隔秒数（避免在短时间内重复发送）
   */
  private long intervalSeconds = 5 * 60;

  public long getIntervalSeconds() {
    return intervalSeconds;
  }

  public void setIntervalSeconds(long intervalSeconds) {
    this.intervalSeconds = intervalSeconds;
  }

  public Map<String, String> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, String> template) {
    this.template = template;
  }

  public String getMultipleMonitorsMultipleWarnings() {
    return this.template.get("multiple-monitors-multiple-warnings");
  }
}
