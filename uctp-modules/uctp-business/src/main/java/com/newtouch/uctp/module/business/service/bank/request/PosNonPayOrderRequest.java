package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * POS商户未支付订单请求
 */
@Data
public class PosNonPayOrderRequest {

    /**
     * pos商户编号
     */
    private String merchantNo;

    /**
     * 订单编号 - 对应平台交易合同号 如果为空拉取商户下所有未支付订单信息
     */
    private String orderNo;
}

