package club.yunzhi.demo.wxhandler;

import club.yunzhi.demo.entity.WeChatUser;
import club.yunzhi.demo.service.WeChatMpService;
import club.yunzhi.demo.wxmessagebuilder.TextBuilder;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Binary Wang
 */
public abstract class AbstractHandler implements WxMpMessageHandler {
  protected WeChatMpService weChatMpService;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public AbstractHandler(WeChatMpService weChatMpService) {
    this.weChatMpService = weChatMpService;
  }

  protected WxMpXmlOutMessage handleByEventKey(String eventKey, WeChatUser weChatUser, WxMpXmlMessage wxMpXmlMessage) {
    WeChatMpEventKeyHandler weChatMpEventKeyHandler = this.weChatMpService.getHandler(eventKey);
    if (weChatMpEventKeyHandler == null) {
      return new TextBuilder().build("该二维码已过期或发生了其它错误，请重试。若您重试仍无法解决该问题，请联系开发人员：13000000000(微信)。",
          wxMpXmlMessage,
          weChatMpService);
    }
    this.weChatMpService.removeHandler(wxMpXmlMessage.getEventKey());
    return weChatMpEventKeyHandler.handle(wxMpXmlMessage, weChatUser);
  }

}
