package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 按余额出金请求报文
 */
@Data
public class BalancesWithdrawalRequest {

    /**
     * 交易地区代码
     */
    private String areaCode;

    /**
     * 监管账号
     */
    private String settleAcctNo;

    /**
     * 子账号
     */
    private String capitalAcctNo;

    /**
     * 商户交易流水号
     */
    private String channelSeqNo;

    /**
     * 出金类型 06-按子账户出金
     */
    private String gldYldTypeCd;

    /**
     * 授权码
     */
    private String authrCd;

    /**
     * 收款方开户行名称
     */
    private String opnBnkNm;

    /**
     * 收款方开户行名称
     */
    private String openBrNo;

    /**
     * 收款方户名
     */
    private String pyAcctNm;

    /**
     * 收款方开户行名称
     */
    private String pyAcctNo;

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
