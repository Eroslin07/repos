package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 订单支付结果查询请求报文
 */
@Data
public class OrdersPayStatusRequest {

    /**
     * 商户编号
     */
    private String mrchNo;

    /**
     * 商户订单号
     */
    private String mrchOrdrNo;

    /**
     * 订单支付日期
     */
    private String pymtDt;
}
