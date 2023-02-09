package club.yunzhi.demo.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author panjie 3792535@qq.com
 * @date 2022/2/26
 * @blog https://segmentfault.com/u/myskies
 * @description 重写请求的Host主机信息
 */
@Component
public class HeaderRequestHostFilter extends OncePerRequestFilter {
  @Override
  public void doFilterInternal(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  FilterChain filterChain) throws ServletException, IOException {
    filterChain.doFilter(new HostHeaderHttpServletRequest(httpServletRequest), httpServletResponse);
  }

  /**
   * 做个装饰器
   * 重写下getRemoteHost()方法，以适用于nginx在后台做转发的情况
   * */
  static public class HostHeaderHttpServletRequest extends HttpServletRequestWrapper {
    private final HttpServletRequest request;

    public HostHeaderHttpServletRequest(HttpServletRequest request) {
      super(request);
      this.request = request;
    }

    @Override
    public String getRemoteHost() {
      String headerHost = this.getHeader("HOST");
      if (null == headerHost) {
        return this.request.getRemoteHost();
      } else {
        return headerHost;
      }
    }
  }
}
