package com.newtouch.uctp.module.business.mq.producer;

import com.newtouch.uctp.framework.mq.core.bus.AbstractBusProducer;
import com.newtouch.uctp.module.business.mq.message.UserAuthMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户认证相关消息的 Producer
 *
 * @author lc
 */
@Slf4j
@Component
public class UserAuthProducer extends AbstractBusProducer {
    //延时10分钟
    public static final String TEN_MINUTES = "14";
    //延时5分钟
    public static final String FIVE_MINUTES = "9";
    public static final String ONE_MINUTES = "5";
    public static final String TWO_MINUTES = "6";

    @Resource
    private StreamBridge streamBridge;

    /**
     * 发送 {@link UserAuthMessage} 消息
     *
     * @param userId 用户id
     * @param contract 用户手机号码
     * @param delayLevel 延迟级别
     */
    public void sendUserAuthMessage(Long userId,String contract,String delayLevel) {
        UserAuthMessage message = new UserAuthMessage()
                .setUserId(userId).setContract(contract);
        Message<UserAuthMessage> springMessage = MessageBuilder.withPayload(message)
                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel) //设置延迟级别为 14，10 分钟后消费
                .build();
        boolean send = streamBridge.send("userAuth-out-0", springMessage);
        log.info("[sendUserAuthMessage]消息发送完成，结果={}",send);
    }
    /**
     * 发送 {@link UserAuthMessage} 消息
     *
     * @param contract 用户手机号码
     * @param delayLevel 延迟级别
     */
    public void sendUserAuthMessage(String contract,String delayLevel) {
        this.sendUserAuthMessage(null,contract,delayLevel);
    }
}
