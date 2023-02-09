package club.yunzhi.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义密码带有超级密码功能的校验器.
 * 注意：其不能够声明为@Component组件出现，否则将触发DaoAuthenticationProvider的构造函数
 * 从而直接注册DelegatingPasswordEncoder校验器
 */
public class SuperPasswordBCryptPasswordEncoder extends BCryptPasswordEncoder {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 一次性密码.
   */
  private final OneTimePassword oneTimePassword;

  public SuperPasswordBCryptPasswordEncoder(OneTimePassword oneTimePassword) {
    super();
    this.oneTimePassword = oneTimePassword;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (rawPassword == null) {
      throw new IllegalArgumentException("rawPassword cannot be null");
    }

    if (oneTimePassword.matches(rawPassword, encodedPassword)) {
      return true;
    }

    return super.matches(rawPassword, encodedPassword);
  }
}
