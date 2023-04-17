package com.newtouch.uctp.framework.qiyuesuo.core.client.impl;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.QysClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 契约锁客户端工厂接口
 *
 * @author lc
 */
@Validated
//@Slf4j
public class QiyuesuoClientFactoryImpl implements QiyuesuoClientFactory {
    /**
     * 短信客户端 Map
     * key：渠道编号，使用 {@link QiyuesuoChannelProperties#getId()}
     */
    private final ConcurrentMap<Long, AbstractQiyuesuoClient> channelIdClients = new ConcurrentHashMap<>();

    /**
     * 短信客户端 Map
     * key：渠道编码，使用 {@link QiyuesuoChannelProperties#getCode()} ()}
     *
     * 注意，一些场景下，需要获得某个渠道类型的客户端，所以需要使用它。
     * 例如说，解析短信接收结果，是相对通用的，不需要使用某个渠道编号的 {@link #channelIdClients}
     */
    private final ConcurrentMap<String, AbstractQiyuesuoClient> channelCodeClients = new ConcurrentHashMap<>();

    public QiyuesuoClientFactoryImpl() {
        // 初始化 channelCodeClients 集合
        Arrays.stream(QiyuesuoChannelEnum.values()).forEach(channel -> {
            // 创建一个空的 QiyuesuoChannelProperties 对象
            QiyuesuoChannelProperties properties = new QiyuesuoChannelProperties().setCode(channel.getCode())
                    .setApiKey("default default").setApiSecret("default");
            // 创建 契约锁 客户端
            AbstractQiyuesuoClient client = createQiyuesuoClient(properties);
            channelCodeClients.put(channel.getCode(), client);
        });
    }

    private AbstractQiyuesuoClient createQiyuesuoClient(QiyuesuoChannelProperties properties) {
        QiyuesuoChannelEnum channelEnum = QiyuesuoChannelEnum.getByCode(properties.getCode());
        Assert.notNull(channelEnum, String.format("渠道类型(%s) 为空", channelEnum));
        // 创建客户端
        switch (channelEnum) {
            case QIYUESUO: return new QysClient(properties);
        }
        // 创建失败，错误日志 + 抛出异常
//        log.error("[createQiyuesuoClient][配置({}) 找不到合适的客户端实现]", properties);
        throw new IllegalArgumentException(String.format("配置(%s) 找不到合适的客户端实现", properties));
    }

    @Override
    public QiyuesuoClient getQiyuesuoClient(Long channelId) {
        return null;
    }

    @Override
    public QiyuesuoClient getQiyuesuoClient(String channelCode) {
        return null;
    }

    @Override
    public void createOrUpdateQiyuesuoClient(QiyuesuoChannelProperties properties) {

    }
}
