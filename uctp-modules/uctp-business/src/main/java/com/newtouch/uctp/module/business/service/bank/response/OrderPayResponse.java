package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 支付订单响应报文
 */
@Data
public class OrderPayResponse {

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
     * 交易返回状态
     */
    private String returnStCd;

    /**
     * 交易描述
     */
    private String tranDsc;
}
