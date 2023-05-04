package com.newtouch.uctp.module.business.mq.consumer;

import com.newtouch.uctp.module.business.mq.message.UserAuthMessage;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * 针对 {@link UserAuthMessage} 的消费者
 *
 * @author lc
 */
@Component
@Slf4j
public class UserAuthConsumer implements Consumer<UserAuthMessage> {

    @Resource
    private QysConfigService qysConfigService;


    @Override
    public void accept(UserAuthMessage userAuthMessage) {
        log.info("[accept][消息内容({})]", userAuthMessage);
        qysConfigService.userAuthResult(userAuthMessage);
    }
}
