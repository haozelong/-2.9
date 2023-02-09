package club.yunzhi.demo.service;
import java.util.HashMap;

/**
 * 微信公众号消息服务
 */
public interface WeChatMpMessageService {

  /**
   * 发送模板消息
   *
   * @param templateId 模板 ID
   * @param messages   消息
   */
  void asyncSendTemplateMessage(String templateId, HashMap<String, String> messages, String openId);
}
