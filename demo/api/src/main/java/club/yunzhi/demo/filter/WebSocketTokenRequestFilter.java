package club.yunzhi.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * WebSocket认证拦截器
 * <p>
 * 拦截握手阶段，在握手阶段完成用户认证
 * 但由于一些原因，未启用。具体参考：WebSocketStompConfig
 * </p>
 */
public class WebSocketTokenRequestFilter extends OncePerRequestFilter {
  private Logger logger = LoggerFactory.getLogger(WebSocketTokenRequestFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    this.logger.debug("uri" + httpServletRequest.getRequestURI());
    if (httpServletRequest.getMethod().equals("GET") && httpServletRequest.getRequestURI().equals("websocket")) {
      Map<String, String[]> map = httpServletRequest.getParameterMap();
      String[] tokens = map.get("ws-auth-token");
      if (tokens != null && tokens.length > 0) {
        // 增加WX认证用户，角色为空
        AnonymousAuthenticationToken anonymousAuth = new AnonymousAuthenticationToken(
            tokens[0],
            tokens[0],
            new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
