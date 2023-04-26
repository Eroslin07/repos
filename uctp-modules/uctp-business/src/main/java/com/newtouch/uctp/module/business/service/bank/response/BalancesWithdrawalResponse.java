package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 按余额出金响应报文
 */
@Data
public class BalancesWithdrawalResponse {

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
     * 交易日期
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 银行方交易流水号
     */
    private String tranSeqNo;

    /**
     * 出金状态
     */
    private String outGldStCd;

    /**
     * 出金金额
     */
    private String gldYldAmt;

    /**
     * 手续费
     */
    private String charges;

    /**
     * 手续费承担方
     */
    private String chrgCmmtPrt;

    /**
     * 收款方开户行名称
     */
    private String openBrNo;

    /**
     * 收款方开户行行号
     */
    private String opnBnkNm;

    /**
     * 收款方户名
     */
    private String pyAcctNm;

    /**
     * 收款方账号
     */
    private String pyAcctNo;

    /**
     * 备用字段1
     */
    private String rsrvFld1;

    /**
     * 备用字段2
     */
    private String rsrvFld2;

    /**
     * 备用字段3 - 出金失败原因
     */
    private String rsrvFld3;
}
