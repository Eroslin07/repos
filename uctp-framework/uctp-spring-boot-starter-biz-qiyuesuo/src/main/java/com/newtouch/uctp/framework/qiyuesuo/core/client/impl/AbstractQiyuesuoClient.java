package com.newtouch.uctp.framework.qiyuesuo.core.client.impl;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCodeMapping;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.response.SdkResponse;

/**
 * 契约锁客户端的抽象类，提供模板方法，减少子类的冗余代码
 *
 * @author lc
 * @since 2021/2/1 9:28
 */
//@Slf4j
public abstract class AbstractQiyuesuoClient implements QiyuesuoClient {
    /**
     * 契约锁渠道配置
     */
    protected volatile QiyuesuoChannelProperties properties;
    /**
     * 错误码枚举类
     */
    protected final QiyuesuoCodeMapping codeMapping;

    public AbstractQiyuesuoClient(QiyuesuoChannelProperties properties, QiyuesuoCodeMapping codeMapping) {
        this.properties = properties;
        this.codeMapping = codeMapping;
    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
//        log.info("[init][配置({}) 初始化完成]", properties);
    }
    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    public final void refresh(QiyuesuoChannelProperties properties) {
        // 判断是否更新
        if (properties.equals(this.properties)) {
            return;
        }
//        log.info("[refresh][配置({})发生变化，重新初始化]", properties);
        this.properties = prepareProperties(properties);
        // 初始化
        this.init();
    }
    /**
     * 在赋值给{@link this#properties}前，子类可根据需要预处理短信渠道配置
     *
     * @param properties 数据库中存储的短信渠道配置
     * @return 满足子类实现的短信渠道配置
     */
    protected QiyuesuoChannelProperties prepareProperties(QiyuesuoChannelProperties properties) {
        return properties;
    }

    @Override
    public Long getId() {
        return properties.getId();
    }

    @Override
    public final QiyuesuoCommonResult<SdkResponse<Contract>> draft(Contract contract) {
        //创建合同草稿
        QiyuesuoCommonResult<SdkResponse<Contract>> result;
        try {
            result = doDraft(contract);
        } catch (Throwable ex) {
            // 打印异常日志
//            log.error("[draft][发起合同草稿异常，contract({}) ]",
//                    contract, ex);
            // 封装返回
            return QiyuesuoCommonResult.error(ex);
        }
        return result;
    }

    protected abstract QiyuesuoCommonResult<SdkResponse<Contract>> doDraft(Contract contract)
            throws Throwable;

    @Override
    public SdkResponse parseQiyuesuoReceiveStatus(String text, Class clazz) throws Throwable {
        return JSONUtils.toQysResponse(text, clazz);
    }
}
