package com.newtouch.uctp.module.business.service.bank.response.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class ResponseHead {

    /**
     * 交易码
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String trCode;

    /**
     * 企业代码
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String corpNo;

    /**
     * 发起方序号
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String reqNo;

    /**
     * 交易序号
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String serialNo;

    /**
     * 应答流水号
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String ansNo;

    /**
     * 下笔交易序号
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String nextNo;

    /**
     * 交易日期
     */
    @JacksonXmlProperty(localName = "tr_code")
    private Long trAcdt;

    /**
     * 时间
     */
    @JacksonXmlProperty(localName = "tr_code")
    private Long trTime;

    /**
     * 返回码
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String ansCode;

    /**
     * 返回信息
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String ansInfo;

    /**
     * 返回附加码
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String particularCode;

    /**
     * 返回附加详细信息
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String particularInfo;

    /**
     * 原子交易数
     */
    @JacksonXmlProperty(localName = "tr_code")
    private Long atomTrCount;

    /**
     * 保留字段
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String reserved;
}
