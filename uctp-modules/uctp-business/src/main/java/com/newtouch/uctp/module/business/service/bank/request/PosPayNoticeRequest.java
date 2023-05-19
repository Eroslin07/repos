package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * POS机支付通知请求
 */
@Data
public class PosPayNoticeRequest {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付状态 0-成功 1-失败 2-其他
     */
    private String state;

    /**
     * 支付消息
     */
    private String message;
}
