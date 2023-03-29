package com.newtouch.uctp.module.system.mq.consumer.mail;

import com.newtouch.uctp.module.system.mq.message.mail.MailTemplateRefreshMessage;
import com.newtouch.uctp.module.system.service.mail.MailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link MailTemplateRefreshMessage} 的消费者
 *
 * @author wangjingyi
 */
@Component
@Slf4j
public class MailTemplateRefreshConsumer {

    @Resource
    private MailTemplateService mailTemplateService;

    @EventListener
    public void onMessage(MailTemplateRefreshMessage message) {
        log.info("[onMessage][收到 Mail Template 刷新信息]");
        mailTemplateService.initLocalCache();
    }

}
