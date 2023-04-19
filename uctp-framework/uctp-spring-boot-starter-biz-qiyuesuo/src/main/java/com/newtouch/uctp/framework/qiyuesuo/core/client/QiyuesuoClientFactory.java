package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;

/**
 * 契约锁客户端的工厂接口
 *
 * @author lc
 * @since 2021/1/28 14:01
 */
public interface QiyuesuoClientFactory {

    /**
     * 获得契约锁 Client
     *
     * @param channelId 渠道编号
     * @return 短信 Client
     */
    QiyuesuoClient getQiyuesuoClient(Long channelId);
    /**
     * 获得契约锁SAAS模式 Client
     *
     * @param channelId 渠道编号
     * @return 短信 Client
     */
    QiyuesuoSaasClient getQiyuesuoSaasClient(Long channelId);

    /**
     * 获得契约锁 Client
     *
     * @param channelCode 渠道编码
     * @return 短信 Client
     */
    QiyuesuoClient getQiyuesuoClient(String channelCode);

    /**
     * 创建契约锁 Client
     *
     * @param properties 配置对象
     */
    void createOrUpdateQiyuesuoClient(QiyuesuoChannelProperties properties);

}
