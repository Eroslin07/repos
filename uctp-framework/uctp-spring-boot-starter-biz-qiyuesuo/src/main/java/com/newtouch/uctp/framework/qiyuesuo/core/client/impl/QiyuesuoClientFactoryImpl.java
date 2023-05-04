package com.newtouch.uctp.framework.qiyuesuo.core.client.impl;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

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
     * 契约锁请求地址
     */
    private String serverUrl;
    /**
     * 契约锁客户端 Map
     * key：渠道编码，使用 {@link QiyuesuoChannelProperties#getCode()} ()}
     *
     * 注意，一些场景下，需要获得某个渠道类型的客户端，所以需要使用它。
     * 例如说，解析短信接收结果，是相对通用的，不需要使用某个渠道编号的 {@link #channelIdClients}
     */
//    private final ConcurrentMap<String, AbstractQiyuesuoClient> channelCodeClients = new ConcurrentHashMap<>();

    public QiyuesuoClientFactoryImpl(String serverUrl) {
        // 初始化 channelCodeClients 集合
//        Arrays.stream(QiyuesuoChannelEnum.values()).forEach(channel -> {
//            // 创建一个空的 QiyuesuoChannelProperties 对象
//            QiyuesuoChannelProperties properties = new QiyuesuoChannelProperties().setCode(channel.getCode())
//                    .setAccessKey("default default").setAccessSecret("default").setServerUrl("");
//            // 创建 契约锁SAAS 客户端
//            AbstractQiyuesuoClient client = createQiyuesuoClient(properties);
//            channelCodeClients.put(channel.getCode(), client);
//        });
        this.serverUrl = serverUrl;
    }

    private AbstractQiyuesuoClient createQiyuesuoClient(QiyuesuoChannelProperties properties) {
        QiyuesuoChannelEnum channelEnum = QiyuesuoChannelEnum.getByCode(properties.getCode());
        Assert.notNull(channelEnum, String.format("渠道类型(%s) 为空", channelEnum));
        properties.setServerUrl(this.serverUrl);
        // 创建客户端
        switch (channelEnum) {
            case DEFAULT: return new DefaultQiyuesuoClient(properties);
            case SAAS: return new SaasQiyuesuoSaasClient(properties);
        }
        // 创建失败，错误日志 + 抛出异常
//        log.error("[createQiyuesuoClient][配置({}) 找不到合适的客户端实现]", properties);
        throw new IllegalArgumentException(String.format("配置(%s) 找不到合适的客户端实现", properties));
    }

    @Override
    public QiyuesuoClient getQiyuesuoClient(Long channelId) {
        return channelIdClients.get(channelId);
    }

    @Override
    public QiyuesuoSaasClient getQiyuesuoSaasClient(Long channelId) {
        return channelIdClients.get(channelId);
    }

//    @Override
//    public QiyuesuoSaasClient getQiyuesuoSaasClient(String channelCode) {
//        return channelCodeClients.get(channelCode);
//    }

    @Override
    public void createOrUpdateQiyuesuoClient(QiyuesuoChannelProperties properties) {
        AbstractQiyuesuoClient client = channelIdClients.get(properties.getId());
        if (client == null) {
            client = this.createQiyuesuoClient(properties);
            client.init();
            channelIdClients.put(client.getId(), client);
        } else {
            client.refresh(properties);
        }
    }
}
