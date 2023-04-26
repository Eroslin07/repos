package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 不明入金清分响应报文
 */
@Data
public class UnKnowClearingResponse {

    /**
     * 返回信息
     */
    private String statusMsg;

    /**
     * 返回状态码
     */
    private String statusCode;

    /**
     * 返回状态码
     */
    private String transNo;

    /**
     * 返回状态码
     */
    private String channelSeqNo;

    /**
     * 原请求渠道流水号
     */
    private String oldPayChannelSeqNo;

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
     * 母账户号
     */
    private String settleAcctNo;

    /**
     * 入金交易流水号
     */
    private String ylkTranSeqNo;

    /**
     * 待清分核心交易流水号
     */
    private String tranSeqNo;

    /**
     * 待清分资金监管交易流水号
     */
    private String orgTranSeqNo;

    /**
     * 清分类型
     */
    private String clrgTp;

    /**
     * 清分状态
     */
    private String clrgLgtyp;

    /**
     * 清分方式
     */
    private String clrgMd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 备用字段
     */
    private String rsrvFld;
}
