package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 订单支付结果查询响应报文
 */
@Data
public class OrdersPayStatusResponse {

    /**
     * 返回信息
     */
    private String statusMsg;

    /**
     * 返回状态码
     */
    private String statusCode;

    /**
     * 交易流水号
     */
    private String transNo;

    /**
     * 交易状态码
     */
    private String returnStCd;

    /**
     * 交易描述
     */
    private String tranDsc;

    /**
     * 商户编号
     */
    private String mrchNo;

    /**
     * 商户订单号
     */
    private String mrchOrdrNo;

    /**
     * 支付日期
     */
    private String pymtDt;

    /**
     * 检索参考号
     */
    private String srchRefrNo;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 交易订单号
     */
    private String tranOrdrNo;

    /**
     * 扫码代号
     */
    private String scanCode;

    /**
     * 交易金额
     */
    private String transAmt;
}
