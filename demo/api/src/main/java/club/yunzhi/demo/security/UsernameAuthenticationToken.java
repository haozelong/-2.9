package club.yunzhi.demo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 仅存在用户名（前面传入的随机字符串）的用户
 * 用于解决WebSocket在登录前只连接一次的问题
 * 由于未使用握手拦截，所以该类未使用，
 */
public class UsernameAuthenticationToken extends AbstractAuthenticationToken {
  private final UserDetails userDetails;

  public UsernameAuthenticationToken(UserDetails userDetails) {
    super((Collection) null);
    this.userDetails = userDetails;
    this.setAuthenticated(true);
  }

  public String getUsername() {
    return this.userDetails.getUsername();
  }

  @Override
  public Object getCredentials() {
    return super.getAuthorities();
  }

  @Override
  public Object getPrincipal() {
    return this.getUsername();
  }
}
