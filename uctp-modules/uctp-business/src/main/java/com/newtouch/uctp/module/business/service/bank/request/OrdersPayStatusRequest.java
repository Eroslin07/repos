package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Builder;
import lombok.Data;

/**
 * 订单支付结果查询请求报文
 */
@Data
@Builder
public class OrdersPayStatusRequest {

    /**
     * 商户编号-必填
     */
    private String mrchNo;

    /**
     * 商户订单号-必填
     */
    private String mrchOrdrNo;

    /**
     * 订单支付日期-必填
     */
    private String pymtDt;
}
