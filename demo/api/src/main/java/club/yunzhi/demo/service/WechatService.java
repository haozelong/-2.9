package club.yunzhi.demo.service;


import club.yunzhi.demo.entity.WeChatUser;

/**
 * @author panjie 3792535@qq.com
 * @date 2021/8/20
 * @blog https://segmentfault.com/u/myskies
 * @description 微信
 */
public interface WechatService {

  WeChatUser getOneByOpenidAndAppId(String openId, String toUser);
  /**
   * 注册
   *
   * @param wechatUser    微信用户
   * @param encryptedData 加密数据
   * @param iv            加密令牌
   * @return 注册成功true, 失败false; sessionKey过期
   */
  boolean register(WeChatUser wechatUser, String encryptedData, String iv);

  /**
   * 发送消息提醒
   * @param openId openId
   * @param key 关键字(同关键字的会合并发送)
   * @param warningMessage 警告信息
   */
  void sendWaringMessage(String openId, String key, String warningMessage, String module);

  void sendMessage();
}
