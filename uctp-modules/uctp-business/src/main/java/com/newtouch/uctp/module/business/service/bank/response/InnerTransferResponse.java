package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 子账号互转响应报文
 */
@Data
public class InnerTransferResponse {

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
     * 响应流水号
     */
    private String tranSeqNo;

    /**
     * 监管账号
     */
    private String settleAcctNo;

    /**
     * 付款方子账号
     */
    private String payerAcctNo;

    /**
     * 付款方子账户名
     */
    private String payerName;

    /**
     * 收款方子账号
     */
    private String payeeAcctNo;

    /**
     * 收款方子账户名
     */
    private String payeeName;

    /**
     * 交易金额
     */
    private String tranAmt;

    /**
     * 备注
     */
    private String remark;

    /**
     * 备用字段1
     */
    private String rsrvFld1;

    /**
     * 备用字段2
     */
    private String rsrvFld2;

    /**
     * 备用字段3
     */
    private String rsrvFld3;
}
