package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 银行虚拟账号生成请求报文
 */
@Data
public class NominalAccountRequest {

    /**
     * 交易日期
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 商户交易流水号
     */
    private String channelSeqNo;

    /**
     * 交易地区代码
     */
    private String areaCode;

    /**
     * 监管账号
     */
    private String acctNo;

    /**
     * 交易席位号
     */
    private String bidsSnglFlgCd;

    /**
     * 交易席位名称
     */
    private String bidsName;

    /**
     * 证件类型
     */
    private String ctfType;

    /**
     * 证件号码
     */
    private String ctfId;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 开户行行号
     */
    private String openBrNo;

    /**
     * 开户行名称
     */
    private String openBranchName;

    /**
     * 投标日期
     */
    private String bidDate;

    /**
     * 投标时间
     */
    private String bidTime;

    /**
     * 开标日期
     */
    private String opnBdDate;

    /**
     * 开标时间
     */
    private String opnBdTime;

    /**
     * 保证金截止日期
     */
    private String mrgnStopDate;

    /**
     * 保证金截止时间
     */
    private String mrgnStopTime;

    /**
     * 基本账户账号
     */
    private String bscAcctNo;

    /**
     * 基本账户名称
     */
    private String acctName;

    /**
     * 标书款
     */
    private String bidsAmt;

    /**
     * 保证金
     */
    private String marginAmt;

    /**
     * 是否退息
     */
    private String rtnIntFlg;

    /**
     * 是否提前查看保证金缴纳家数
     */
    private String isFlag;

    /**
     * 到期日
     */
    private String endDt;

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
