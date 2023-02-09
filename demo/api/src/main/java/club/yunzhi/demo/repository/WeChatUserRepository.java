package club.yunzhi.demo.repository;


import club.yunzhi.demo.entity.WeChatUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 微信(公众号、小程序)用户
 */
public interface WeChatUserRepository extends CrudRepository<WeChatUser, Long> {

  Optional<WeChatUser> findByOpenid(String openid);

  Optional<WeChatUser> findByOpenidAndAppId(String openid, String appId);
}
