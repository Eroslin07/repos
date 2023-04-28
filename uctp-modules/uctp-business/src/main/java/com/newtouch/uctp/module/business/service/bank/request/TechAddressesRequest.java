package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 场景转账链接生成交易请求报文
 */
@Data
public class TechAddressesRequest {

    /**
     * 订单号 - 我方生成
     */
    private String ordrNo;

    /**
     * 商户号 - 银行提供 固定值
     */
    private String mrchId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 付款账号
     */
    private String pymtAcctNo;

    /**
     * 收款账号
     */
    private String payeeAcctNo;

    /**
     * 转账金额
     */
    private String tfrAmount;

    /**
     * ios回跳地址
     */
    private String returnAdr;

    /**
     * android回跳地址
     */
    private String olRdctnAdr;

    /**
     * H5回跳地址
     */
    private String address;
}
