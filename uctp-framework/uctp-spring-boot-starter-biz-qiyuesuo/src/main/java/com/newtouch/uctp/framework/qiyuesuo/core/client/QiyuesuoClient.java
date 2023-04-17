package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.response.SdkResponse;

/**
 * 用于对接各契约锁平台的 SDK，实现合同发送等功能
 *
 * @author lc
 * @since 2021/1/25 14:14
 */
public interface QiyuesuoClient {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

    /**
     * 创建合同草稿
     * @param draftContract 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<SdkResponse<Contract>> draft(Contract draftContract);

    /**
     * 解析接收契约锁的接收结果
     *
     * @param text 结果
     * @return 结果内容
     * @throws Throwable 当解析 text 发生异常时，则会抛出异常
     */
     SdkResponse parseQiyuesuoReceiveStatus(String text, Class clazz) throws Throwable;

}
