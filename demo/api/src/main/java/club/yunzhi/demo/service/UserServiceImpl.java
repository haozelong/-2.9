package club.yunzhi.demo.service;

import club.yunzhi.demo.entity.User;
import club.yunzhi.demo.entity.WeChatUser;
import club.yunzhi.demo.repository.UserRepository;
import club.yunzhi.demo.repository.WeChatUserRepository;
import club.yunzhi.demo.vo.VUser;
import club.yunzhi.demo.vo.ValidateMessage;
import com.mengyunzhi.core.exception.ValidationException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.*;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final WeChatUserRepository weChatUserRepository;


  /**
   * 重置后的密码
   */
  private String initialPassword = "yunzhi";

  public UserServiceImpl(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         WeChatUserRepository weChatUserRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.weChatUserRepository = weChatUserRepository;
  }

  @Override
  public User findByUsername(String username) {
    return null;
  }

  @Override
  public List<User> getAll() {
    return (List<User>) this.userRepository.findAll();
  }

  @Override
  public Optional<User> getCurrentLoginUser() {
    logger.debug("初始化用户");
    Optional<User> user = null;

    logger.debug("获取用户认证信息");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    logger.debug("根据认证信息查询用户");
    if (authentication != null && authentication.isAuthenticated()) {
      user = userRepository.findByUsername(authentication.getName());
    }

    return user;
  }


  private boolean existByUsername(String username) {
    return this.userRepository.findByUsername(username) != null;
  }

  @Override
  public boolean checkPasswordIsRight(VUser vUser) {
    logger.debug("获取当前用户");
    Optional<User> user = this.getCurrentLoginUser();

    logger.debug("比较密码是否正确");
    return this.passwordEncoder.matches(vUser.getPassword(), user.get().getPassword());
  }

  @Override
  public void updatePassword(VUser vUser) {
    logger.debug("获取当前用户");
    Optional<User> currentUser = this.getCurrentLoginUser();

    logger.debug("校验原密码是否正确");
    if (!this.checkPasswordIsRight(vUser)) {
      throw new ValidationException("原密码不正确");
    }

    logger.debug("更新密码");
    currentUser.get().setPassword(vUser.getNewPassword());
    this.userRepository.save(currentUser.get());
  }


  @Override
  public Page<User> getAll(String name, String username, Pageable pageable) {
    Assert.notNull(pageable, "Pageable不能为null");
    return this.userRepository.getAll(name, username, pageable);
  }

  @Override
  public User getByUsername(String name) {
    return this.userRepository.findByUsername(name).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("未找到相关用户"));
  }

  @Override
  public void update(Long id, User user) {
    Assert.notNull(user.getName(), "用户名称不能为空");
    Assert.notNull(user.getUsername(), "用户用户名不能为空");

    User student1 = findById(id);
    student1.setName(user.getName());
    student1.setUsername(user.getUsername());
    userRepository.save(student1);
  }


  @Override
  public void delete(Long id) {
    User user = findById(id);
    // 删除用户
    userRepository.deleteById(user.getId());
  }

  @Override
  public User save(User user) {
    Assert.notNull(user.getName(), "用户名称不能为空");
    Assert.notNull(user.getUsername(), "用户用户名不能为空");
    User newUser = new User();
    newUser.setName(user.getName());
    newUser.setUsername(user.getUsername());
    newUser.setPassword(initialPassword);
    // 设置用户的角色为普通用户

    return this.userRepository.save(newUser);
  }



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

    // 设置用户角色
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
  }
}
