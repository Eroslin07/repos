package com.newtouch.uctp.module.system.mq.producer.permission;

import com.newtouch.uctp.framework.mq.core.bus.AbstractBusProducer;
import com.newtouch.uctp.module.system.mq.message.permission.RoleRefreshMessage;
import org.springframework.stereotype.Component;

/**
 * Role 角色相关消息的 Producer
 *
 * @author 芋道源码
 */
@Component
public class RoleProducer extends AbstractBusProducer {

    /**
     * 发送 {@link RoleRefreshMessage} 消息
     */
    public void sendRoleRefreshMessage() {
        publishEvent(new RoleRefreshMessage(this, getBusId(), selfDestinationService()));
    }

}
