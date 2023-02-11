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
}
