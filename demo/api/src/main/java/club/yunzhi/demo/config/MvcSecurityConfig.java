package club.yunzhi.demo.config;

import club.yunzhi.demo.entity.User;
import club.yunzhi.demo.filter.HeaderRequestHostFilter;
import club.yunzhi.demo.security.OneTimePassword;
import club.yunzhi.demo.security.SuperPasswordBCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableWebSecurity
@EnableSpringHttpSession
public class MvcSecurityConfig extends WebSecurityConfigurerAdapter {

  private final BCryptPasswordEncoder passwordEncoder;
  private final HeaderRequestHostFilter headerRequestHostFilter;

  public MvcSecurityConfig(OneTimePassword oneTimePassword,
                           HeaderRequestHostFilter headerRequestHostFilter) {
    this.passwordEncoder = new SuperPasswordBCryptPasswordEncoder(oneTimePassword);
    this.headerRequestHostFilter = headerRequestHostFilter;
    User.setPasswordEncoder(this.passwordEncoder);
  }

  /**
   * https://spring.io/guides/gs/securing-web/
   *
   * @param http http安全
   * @throws Exception 异常
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        // 开放端口
        .antMatchers("/h2-console/**").permitAll()
        .antMatchers("/Data").permitAll()
        .antMatchers("/user/resetPassword").permitAll()
        .antMatchers("/user/getLoginQrCode/**").permitAll()
        .antMatchers("/user/wxToken").permitAll()
        .antMatchers("/wechat/**").permitAll()
        .antMatchers("/websocket/**").permitAll()
        .antMatchers("/user/sendVerificationCode", "/favicon.ico").permitAll()
        .anyRequest().authenticated()
        .and()
        // 添加通过header获取host信息的过滤器
        // 过滤器执行链请参考：https://docs.spring.io/spring-security/site/docs/5.5.1/reference/html5/#servlet-security-filters
        .addFilterBefore(this.headerRequestHostFilter, BasicAuthenticationFilter.class)
        // 添加微信认证过滤器
        .httpBasic()
        .and().cors()
        .and().csrf().disable();
    http.headers().frameOptions().disable();
  }

  /**
   * 使用header认证来替换默认的cookie认证
   */
  @Bean
  public HttpSessionStrategy httpSessionStrategy() {
    return new HeaderAndParamHttpSessionStrategy();
  }

  /**
   * 由于我们启用了@EnableSpringHttpSession后，而非RedisHttpSession.
   * 所以应该为SessionRepository提供一个实现。
   * 而Spring中默认给了一个SessionRepository的实现MapSessionRepository.
   *
   * @return session策略
   */
  @Bean
  public SessionRepository sessionRepository() {
    return new MapSessionRepository();
  }


  @Bean
  PasswordEncoder passwordEncoder() {
    return this.passwordEncoder;
  }
}
