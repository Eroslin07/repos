package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 不明入金清分请求报文
 */
@Data
public class UnKnowClearingRequest {

    /**
     * 交易日期
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 交易地区代码
     */
    private String areaCode;

    /**
     * 监管账号
     */
    private String settleAcctNo;

    /**
     * 渠道流水号
     */
    private String channelSeqNo;

    /**
     * 待清分核心交易流水号
     */
    private String ylkTranSeqNo;

    /**
     * 待清分资金监管交易流水号
     */
    private String orgTranSeqNo;

    /**
     * 清分类型
     */
    private String clrgTp;

    /**
     * 清分内容
     */
    private String clrgRsltDsc;

    /**
     * 备用字段
     */
    private String rsrvFld;

    /**
     * 备用字段1
     */
    private String rsrvFld1;

    /**
     * 备用字段2
     */
    private String rsrvFld2;

}
