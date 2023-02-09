package club.yunzhi.demo.service;

import club.yunzhi.demo.config.AppWechatConfig;
import club.yunzhi.demo.entity.User;
import club.yunzhi.demo.entity.WeChatUser;
import club.yunzhi.demo.repository.UserRepository;
import club.yunzhi.demo.repository.WeChatUserRepository;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author panjie 3792535@qq.com
 * @date 2021/8/20
 * @blog https://segmentfault.com/u/myskies
 * @description
 */
@Service
public class WechatServiceImpl implements WechatService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final WxMaService wxMaService;
  private final UserRepository userRepository;
  private final WeChatUserRepository weChatUserRepository;
  private final Map<String, Message> messageMap = new ConcurrentHashMap();
  private final AppWechatConfig appWechatConfig;
  private final WeChatMpMessageService weChatMpMessageService;

  public WechatServiceImpl(WxMaService wxMaService,
                           UserRepository userRepository,
                           WeChatUserRepository weChatUserRepository,
                           AppWechatConfig appWechatConfig,
                           WeChatMpMessageService weChatMpMessageService) {
    this.wxMaService = wxMaService;
    this.userRepository = userRepository;
    this.weChatUserRepository = weChatUserRepository;
    this.appWechatConfig = appWechatConfig;
    this.weChatMpMessageService = weChatMpMessageService;
  }

  @Override
  public boolean register(WeChatUser wechatUser, String encryptedData, String iv) {
    try {
      WxMaPhoneNumberInfo wxMaPhoneNumberInfo = this.wxMaService.getUserService().getPhoneNoInfo(
          wechatUser.getSessionKey(),
          encryptedData,
          iv);
      if (!wxMaPhoneNumberInfo.getCountryCode().equals("86")) {
        throw new NotSupportedException("当前仅支持国内手机号绑定注册");
      }
      String phoneNumber = wxMaPhoneNumberInfo.getPurePhoneNumber();
      Optional<User> optionalUser = this.userRepository.findByUsername(phoneNumber);
      if (!optionalUser.isPresent()) {
        return false;
      }

      User user = optionalUser.get();
      user.setOpenid(wechatUser.getOpenid());
      this.userRepository.save(user);

      wechatUser.setUser(user);
      this.weChatUserRepository.save(wechatUser);
    } catch (Exception e) {
      this.logger.error("在获取用户手机号信息时发生错误", e.getMessage());
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public WeChatUser getOneByOpenidAndAppId(String openId, String appId) {
    return this.weChatUserRepository.findByOpenidAndAppId(openId, appId)
        .orElseGet(() -> this.weChatUserRepository.save(new WeChatUser(openId, appId)));
  }

  @Override
  public void sendWaringMessage(String openId, String key, String warningMessage, String module) {
    if (openId == null) {
      this.logger.error("未在微信用户上获取到openid");
      return;
    }

    if (this.messageMap.containsKey(openId)) {
      this.messageMap.get(openId).addMessage(key, warningMessage);
    } else {
      this.messageMap.put(openId, new Message(key, warningMessage, this, openId,
          this.appWechatConfig.getIntervalSeconds() * 1000, module));
    }
    this.sendMessage(openId);
  }

  /**
   * 每隔一定时间进行消息发送，防止在一定时间内多次向微信用户发送消息
   */
  @Override
  public void sendMessage() {
    for (Map.Entry<String, Message> entry : this.messageMap.entrySet()) {
      entry.getValue().sendMessage();
    }
  }

  /**
   * 向一个用户发送单个警告消息
   * @param messages 警告消息
   * @param openId 微信id
   * @param module 模块
   */
  public void sendOneWarningMessage(Map.Entry<String, List<String>> messages, String openId, String module) {
    if (messages.getValue().size() == 0) {
      return;
    } else if (messages.getValue().size() == 1) {
      this.sendMessage(openId, "阀值溢出警告", messages.getKey(), "警告", module,
          "监测到超出阀值",
          messages.getValue().get(0));
    } else {
      this.sendMessage(openId, "阀值溢出警告", messages.getKey(), "警告", module,
          String.format("最近共发生了%s次警告", messages.getValue().size()),
          String.format("最后一次警告信息：%s", messages.getValue().get(messages.getValue().size() - 1))
          );
    }
  }

  /**
   * 向一个用户发送多个警告消息
   * @param messages 警告消息
   * @param openId 微信id
   * @param module 模块
   */
  public void sendMultipleWarningMessage(Map<String, List<String>> messages, String openId, String module) {
    if (messages.entrySet().size() != 0) {
      if (messages.entrySet().size() == 1) {
        this.sendOneWarningMessage(messages.entrySet().iterator().next(), openId, module);
      } else {
        String monitor0 = "";
        int warningCount = 0;
        String warningMessage0 = "";
        String warningMessage1 = "";

        int i = 0;
        for (Map.Entry<String, List<String>> entry : messages.entrySet()) {
          if (i == 0) {
            monitor0 = entry.getKey();
            warningMessage0 = entry.getValue().size() > 0 ? entry.getValue().get(0) : "";
          } else if (i == 1) {
            warningMessage1 = entry.getValue().size() > 0 ? entry.getValue().get(0) : "";
          }
          warningCount += entry.getValue().size();
          i++;
        }

        this.sendMessage(openId, "阀值溢出警告", monitor0 + "等", "警告", module,
            String.format("最近共发生%s次警告信息", warningCount),
            warningMessage0 + "、" + warningMessage1 + " 等");
      }
    }
  }

  /**
   * {{first.DATA}} 报警类型：{{keyword1.DATA}} 报警设备：{{keyword2.DATA}} 报警时间：{{keyword3.DATA}} {{remark.DATA}}
   * @param openId openid
   * @param title 标题
   * @param id 标识
   * @param level 等级
   * @param module 模拟
   * @param description 描述
   * @param remark 备注
   */
  public void sendMessage(String openId, String title, String id, String level, String module, String description, String remark) {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+8"));
    HashMap<String, String> map = new HashMap<>();
    map.put("first", title);
    map.put("keyword2", id);
    map.put("keyword3", now.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withZone(ZoneId.of("GMT+8"))));
    map.put("keyword1", level);
    map.put("keyword4", module);
    map.put("keyword5", description);
    map.put("remark", remark);
    this.weChatMpMessageService.asyncSendTemplateMessage(this.appWechatConfig.getMultipleMonitorsMultipleWarnings(), map, openId);
  }


  /**
   * 向某个微信用户发送缓存消息
   * @param openId openId
   */
  public void sendMessage(String openId) {
    Message message = this.messageMap.get(openId);
    if (message == null) {
      return;
    }

    message.sendMessage();
  }

  /**
   * 微信消息
   */
  public static class Message {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 过期时间
     */
    private final long expiredTime;
    private final WechatServiceImpl wechatService;
    /**
     * 接收信息的对应的openid
     */
    private final String openId;
    private final String module;
    /**
     * 上次发送时间(在指定的时间内，最多发送一次)
     */
    Calendar lastSendTime;
    /**
     * 缓存尚未发送的消息
     */
    Map<String, List<String>> messages = new ConcurrentHashMap<>();

    public Message(String key, String warningMessage, WechatServiceImpl wechatService, String openId, long expiredTime, String module) {
      this.expiredTime = expiredTime;
      this.wechatService = wechatService;
      this.openId = openId;
      this.messages.put(key, new ArrayList<String>(Arrays.asList(warningMessage)));
      this.module = module;
    }

    public void addMessage(String key, String warningMessage) {
      if (this.messages.containsKey(key)) {
        this.messages.get(key).add(warningMessage);
      } else {
        this.messages.put(key, new ArrayList<String>(Arrays.asList(warningMessage)));
      }
    }

    public void clearMessage() {
      this.messages.clear();
    }

    public boolean shouldSendMessage() {
      if (this.lastSendTime == null) {
        return true;
      } else {
        this.logger.info(String.valueOf(System.currentTimeMillis() - this.lastSendTime.getTimeInMillis()));
        return System.currentTimeMillis() - this.lastSendTime.getTimeInMillis() >= this.expiredTime;
      }
    }

    public Calendar getLastSendTime() {
      return lastSendTime;
    }

    public void setLastSendTime(Calendar lastSendTime) {
      this.lastSendTime = lastSendTime;
    }

    public Map<String, List<String>> getMessages() {
      return messages;
    }

    public void setMessages(Map<String, List<String>> messages) {
      this.messages = messages;
    }

    public void sendMessage() {
      if (this.shouldSendMessage()) {
        this.setLastSendTime(Calendar.getInstance());
        this.wechatService.sendMultipleWarningMessage(this.messages, this.openId, this.module);
        this.clearMessage();
      }
    }
  }
}
