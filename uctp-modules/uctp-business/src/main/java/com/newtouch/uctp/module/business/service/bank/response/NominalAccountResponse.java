package com.newtouch.uctp.module.business.service.bank.response;

import lombok.Data;

/**
 * 银行虚拟账号生成响应报文
 */
@Data
public class NominalAccountResponse {

    /**
     * 返回
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
    private String channelSeqNo;
    /**
     * 交易日期
     */
    private String tranDate;
    /**
     * 交易时间
     */
    private String tranTime;
    /**
     * 客户号
     */
    private String pcpClntNo;
    /**
     * 子账户名
     */
    private String childAcctNm;
    /**
     * 子账号
     */
    private String childAcctNo;
    /**
     * 授权码
     */
    private String authrCd;
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
