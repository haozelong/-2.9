package club.yunzhi.demo.controller;

import club.yunzhi.demo.entity.User;
import club.yunzhi.demo.security.YunzhiSecurityRole;
import club.yunzhi.demo.service.UserService;
import club.yunzhi.demo.vo.VUser;
import club.yunzhi.demo.vo.ValidateMessage;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author haozelong
 */
@RestController
@RequestMapping("user")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 校验密码是否正确
   *
   * @param vUser 带有密码的VUser
   * @return true 正确 false 不正确
   */
  @PostMapping("checkPasswordIsRight")
  public boolean checkPasswordIsRight(@RequestBody VUser vUser) {
    return this.userService.checkPasswordIsRight(vUser);
  }

  /**
   * 删除用户
   *
   * @param id
   */
  @DeleteMapping("{id}")
  @Secured(YunzhiSecurityRole.ROLE_ADMIN)
  public void delete(@PathVariable Long id) {
    this.userService.delete(id);
  }

  @GetMapping
  @JsonView(GetAllJsonView.class)
  public List<User> getAll() {
    return this.userService.getAll();
  }

  /**
   * 生成绑定微信的二维码
   * @param httpSession session
   * @return 二维码对应的系统ID(用于触发扫码后的回调)
   */
  @GetMapping("generateBindQrCode")
  public String generateBindQrCode(HttpSession httpSession) {
    System.out.println(httpSession.getId());
    return this.userService.generateBindQrCode(httpSession.getId());
  }

  /**
   * 通过id获取用户
   *
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @Secured(YunzhiSecurityRole.ROLE_ADMIN)
  public User getById(@PathVariable Long id) {
    return this.userService.findById(id);
  }

  @GetMapping("currentLoginUser")
  @JsonView(GetCurrentLoginUserJsonView.class)
  public User getCurrentLoginUser(HttpSession httpSession, Principal principal) {
    return principal == null ? null : this.userService.getCurrentLoginUser().get();
  }

  @RequestMapping("login")
  @JsonView(LoginJsonView.class)
  public User login(Principal user) {
    return this.userService.getByUsername(user.getName());
  }

  @GetMapping("logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    logger.info("用户注销");
    // 获取用户认证信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 存在认证信息，注销
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
  }

  /**
   * 获取登录的二维码
   * @param wsAuthToken webSocket认证token
   * @param httpSession session
   * @return 二维码对应的系统ID(用于触发扫码后的回调)
   */
  @GetMapping("getLoginQrCode/{wsAuthToken}")
  public String getLoginQrCode(@PathVariable String wsAuthToken, HttpSession httpSession) {
    return this.userService.getLoginQrCode(wsAuthToken, httpSession);
  }

  /**
   * 获取所有用户
   *
   * @param pageable 分页信息
   * @return 所有用户
   */
  @GetMapping("page")
  @JsonView(PageJsonView.class)
  @Secured(YunzhiSecurityRole.ROLE_ADMIN)
  public Page<User> page(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String username,
      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC)
          Pageable pageable) {
    return this.userService.getAll(
        name,
        username,
        pageable);
  }

  /**
   * 保存用户
   *
   * @param user
   * @return
   */
  @PostMapping("add")
  @JsonView(AddUser.class)
  public User save(@RequestBody User user) {
    return userService.save(user);
  }

  /**
   * 更新用户信息;
   *
   * @param id
   * @param user
   */
  @PutMapping("{id}")
  @Secured(YunzhiSecurityRole.ROLE_ADMIN)
  public void update(@PathVariable Long id, @RequestBody User user) {
    userService.update(id, user);
  }

  /**
   * 修改密码
   *
   * @param vUser 带有新密码和旧密码VUser
   */
  @PutMapping("updatePassword")
  public void updatePassword(@RequestBody VUser vUser) {
    this.userService.updatePassword(vUser);
  }

  public interface GetCurrentLoginUserJsonView extends User.NameJsonView,
      User.UsernameJsonView,
      User.RoleJsonView,
      User.WeChatUserJsonView {
  }

  public interface PageJsonView extends User.UsernameJsonView, User.NameJsonView, User.RoleJsonView,  User.WeChatUserJsonView {
  }

  public interface LoginJsonView extends User.UsernameJsonView, User.NameJsonView, User.RoleJsonView, User.WeChatUserJsonView {
  }

  public interface AddUser extends User.UsernameJsonView, User.NameJsonView {
  }

  public interface ResetPasswordJsonView {
  }

  private class GetAllJsonView implements User.NameJsonView, User.RoleJsonView, User.WeChatUserJsonView {
  }
}
