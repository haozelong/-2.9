package club.yunzhi.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author panjie 3792535@qq.com
 * @date 2022/11/16
 * @blog https://segmentfault.com/u/myskies
 * @description
 */
@Configuration
public class WxMpConfig {
  @Value("${wx.mp.token}")
  private String token;

  @Value("${wx.mp.appid}")
  private String appid;

  @Value("${wx.mp.secret}")
  private String appSecret;

  @Value("${wx.mp.aesKey}")
  private String aesKey;

  public String getToken() {
    return this.token;
  }

  public String getAppid() {
    return this.appid;
  }

  public String getAppSecret() {
    return this.appSecret;
  }

  public String getAesKey() {
    return this.aesKey;
  }
}
