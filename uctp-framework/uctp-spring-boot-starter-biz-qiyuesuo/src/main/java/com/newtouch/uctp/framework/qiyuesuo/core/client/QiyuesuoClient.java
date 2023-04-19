package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.qiyuesuo.sdk.v2.bean.Contract;

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
     * 发起合同草稿
     * @param draftContract 契约锁接口接受的参数
     * @return
     */
    QiyuesuoCommonResult<Contract> defaultSend(Contract draftContract);

}
