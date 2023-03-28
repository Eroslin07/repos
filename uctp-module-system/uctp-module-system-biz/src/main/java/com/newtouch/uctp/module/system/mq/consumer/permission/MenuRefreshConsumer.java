package com.newtouch.uctp.module.system.mq.consumer.permission;

import com.newtouch.uctp.module.system.mq.message.permission.MenuRefreshMessage;
import com.newtouch.uctp.module.system.service.permission.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link MenuRefreshMessage} 的消费者
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class MenuRefreshConsumer {

    @Resource
    private MenuService menuService;

    @EventListener
    public void execute(MenuRefreshMessage menuRefreshMessage) {
        log.info("[execute][收到 Menu 刷新消息]");
        menuService.initLocalCache();
    }
}
