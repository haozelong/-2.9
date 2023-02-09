package club.yunzhi.demo.security;

import java.util.Optional;

/**
 * 一次性密码.
 */
public interface OneTimePassword {

  /**
   * 获取计算完后的结果
   *
   * @return
   */
  Optional<String> getPassword();

  /**
   * 密码匹配
   * @param rawPassword 加密前的密码
   * @param encodedPassword 加密后的密码
   * @return 匹配成功：true
   */
  boolean matches(CharSequence rawPassword, String encodedPassword);
}
