package club.yunzhi.demo.security;

import club.yunzhi.demo.properties.AppProperties;
import club.yunzhi.demo.service.UserService;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class OneTimePasswordImpl implements OneTimePassword {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  /**
   * 密码.
   */
  private String password = "";

  private final String token;

  @Autowired @Lazy
  private UserService userService;

  public OneTimePasswordImpl(AppProperties appProperties) {
    // 将token使用base32进行转码，原理同base64
    Base32 base32 = new Base32();
    // 此处的token并不是"YunZhi.clu8",详情请看26行打印结果或配置文件
    this.token = base32.encodeAsString(appProperties.getToken().getBytes());
  }

  /**
   * 仅允许获取1次，获取成功后code值为null
   *
   * @return
   */
  @Override
  public Optional<String> getPassword() {
    try {
      String password = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(this.token);
      // 每个密码只能用一次，如果生成的密码与当前的密码相同，则说明短时间内请求了两次，返回empty
      if (password.equals(this.password)) {
        return Optional.empty();
      } else {
        this.password = password;
      }
    } catch (GeneralSecurityException e) {
      this.logger.error("生成一次性密码时发生错误");
      e.printStackTrace();
    }

    return Optional.of(this.password);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    // 增加微信扫码后使用webSocket uuid充当用户名与密码进行认证
    if (this.userService.checkWeChatLoginUuidIsValid(rawPassword.toString())) {
      if (this.logger.isDebugEnabled()) {
        this.logger.info("校验微信扫码登录成功");
      }
      return true;
    }

    // 当有一次性密码（每个密码仅能用一次）且未使用时，验证用户是否输入了超密
    Optional oneTimePassword = this.getPassword();
    return oneTimePassword.isPresent() && oneTimePassword.get().equals(rawPassword.toString());
  }
}
