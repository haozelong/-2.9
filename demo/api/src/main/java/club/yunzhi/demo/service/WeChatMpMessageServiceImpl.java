package club.yunzhi.demo.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class WeChatMpMessageServiceImpl implements WeChatMpMessageService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final WxMpService wxMpService;

  public WeChatMpMessageServiceImpl(WxMpService wxMpService) {
    this.wxMpService = wxMpService;
  }

  /**
   * 发送模板消息
   *
   * @param templateId 模板 ID
   * @param messages   消息
   */
  @Async
  @Override
  public void asyncSendTemplateMessage(String templateId, HashMap<String, String> messages, String openId) {
    this.logger.info("我应该是另一个线程");
    WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
        .toUser(openId)
        .templateId(templateId)
        .build();

    for (Map.Entry<String, String> message : messages.entrySet()) {
      templateMessage.addData(new WxMpTemplateData(message.getKey(), message.getValue()));
    }

    try {
      this.logger.debug("异步发送模板消息");
      Thread.sleep(Math.abs(new Random().nextInt() % 5000));
      this.wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
      this.logger.debug("请求完毕");
    } catch (WxErrorException | InterruptedException e) {
      this.logger.error(String.format("发送模板消息时发生错误: %s", e.getMessage()));
      e.printStackTrace();
    }
  }
}
