package club.yunzhi.demo.service;

import club.yunzhi.demo.entity.WeChatUser;
import club.yunzhi.demo.repository.WeChatUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author panjie 3792535@qq.com
 * @date 2021/8/20
 * @blog https://segmentfault.com/u/myskies
 * @description
 */
@Service
public class WechatServiceImpl implements WechatService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final WeChatUserRepository weChatUserRepository;


  public WechatServiceImpl(
                           WeChatUserRepository weChatUserRepository) {
    this.weChatUserRepository = weChatUserRepository;
  }


  @Override
  public WeChatUser getOneByOpenidAndAppId(String openId, String appId) {
    return this.weChatUserRepository.findByOpenidAndAppId(openId, appId)
        .orElseGet(() -> this.weChatUserRepository.save(new WeChatUser(openId, appId)));
  }
}
