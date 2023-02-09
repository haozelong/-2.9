package club.yunzhi.demo.properties;

import com.mengyunzhi.core.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Component
public class AppProperties {
  private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

  private String name = "超级管理员";

  /**
   * 超级用户名
   */
  private String username = "15383011312";

  /**
   * 超级用户对应的密码
   */
  private String password = "admin";

  /**
   * 超级token，用于登录一切用户
   */
  private String token = "YunZhi.clu8";

  private List<String> origins = Arrays.asList("*");

  private short dischargeWarningValue = 150;

  private short dischargeErrorValue = 150;

  /**
   * 短信配置信息 -- 以对象的形式存储短信配置.
   */
  private ShortMessageProperties shortMessageProperties;

  public AppProperties(ShortMessageProperties shortMessageProperties) {
    this.shortMessageProperties = shortMessageProperties;
  }

  private String smsType;

  public String getSmsType() {
    return this.smsType;
  }

  public void setSmsType(String smsType) {
    if ("local".equals(smsType) || "ali".equals(smsType)) {
      this.smsType = smsType;
    } else {
      throw new ValidationException("短信类型仅支持(local,ali),请检查");
    }
  }

  public short getDischargeWarningValue() {
    return dischargeWarningValue;
  }

  public void setDischargeWarningValue(short dischargeWarningValue) {
    this.dischargeWarningValue = dischargeWarningValue;
  }

  public short getDischargeErrorValue() {
    return dischargeErrorValue;
  }

  public void setDischargeErrorValue(short dischargeErrorValue) {
    this.dischargeErrorValue = dischargeErrorValue;
  }

  private void setShortMessageProperties(ShortMessageProperties shortMessageProperties) {
    this.shortMessageProperties = shortMessageProperties;
  }

  public ShortMessageProperties getShortMessageProperties() {
    return this.shortMessageProperties;
  }


  public AppProperties() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return this.username;
  }

  public List<String> getOrigins() {
    return origins;
  }

  public void setOrigins(List<String> origins) {
    this.origins = origins;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
