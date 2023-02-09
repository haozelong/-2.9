package club.yunzhi.demo.wxhandler;

import club.yunzhi.demo.service.WeChatMpService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 门店审核事件处理
 *
 * @author Binary Wang
 */
@Component
public class StoreCheckNotifyHandler extends AbstractHandler {
  public StoreCheckNotifyHandler(WeChatMpService weChatMpService) {
    super(weChatMpService);
  }

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) {
    // TODO 处理门店审核事件
    return null;
  }

}
