package com.newtouch.uctp.framework.qiyuesuo.core.client.impl;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCodeMapping;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.request.SaaSUserAuthPageRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;

/**
 * 契约锁客户端的抽象类，提供模板方法，减少子类的冗余代码
 *
 * @author lc
 * @since 2021/2/1 9:28
 */
//@Slf4j
public abstract class AbstractQiyuesuoClient implements QiyuesuoClient, QiyuesuoSaasClient {
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
     * 在赋值给{@link this#properties}前，子类可根据需要预处理渠道配置
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
//==============================正常模式方法========================================
    @Override
    public final QiyuesuoCommonResult<Contract> draft(Contract contract) {
        QiyuesuoCommonResult<Contract> result;
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

    protected abstract QiyuesuoCommonResult<Contract> doDraft(Contract contract)
            throws Throwable;
//==============================SAAS模式方法========================================


    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> companyAuthPageUrl(SaasCompanyAuthPageUrlRequest request) {
        QiyuesuoCommonResult<SaaSCompanyAuthPageResult> result;
        try {
            result = doCompanyAuthPageUrl(request);
        } catch (Throwable ex) {
            // 打印异常日志
//            log.error("[draft][发起合同草稿异常，contract({}) ]",
//                    contract, ex);
            // 封装返回
            return QiyuesuoCommonResult.error(ex);
        }
        return result;
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> userAuthPage(SaaSUserAuthPageRequest request) {
        QiyuesuoCommonResult<SaaSUserAuthPageResult> result;
        try {
            result = doUserAuthPage(request);
        } catch (Throwable ex) {
            // 打印异常日志
//            log.error("[draft][发起合同草稿异常，contract({}) ]",
//                    contract, ex);
            // 封装返回
            return QiyuesuoCommonResult.error(ex);
        }
        return result;
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> privilegeUrl(SaasPrivilegeUrlRequest request) {
        QiyuesuoCommonResult<SaaSPrivilegeUrlResult> result;
        try {
            result = doPrivilegeUrl(request);
        } catch (Throwable ex) {
            // 打印异常日志
//            log.error("[draft][发起合同草稿异常，contract({}) ]",
//                    contract, ex);
            // 封装返回
            return QiyuesuoCommonResult.error(ex);
        }
        return result;
    }

    protected abstract QiyuesuoCommonResult<SaaSPrivilegeUrlResult> doPrivilegeUrl(SaasPrivilegeUrlRequest request)
            throws Throwable;

    protected abstract QiyuesuoCommonResult<SaaSUserAuthPageResult> doUserAuthPage(SaaSUserAuthPageRequest request)
            throws Throwable;


    protected abstract QiyuesuoCommonResult<SaaSCompanyAuthPageResult> doCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request)
            throws Throwable;
}
