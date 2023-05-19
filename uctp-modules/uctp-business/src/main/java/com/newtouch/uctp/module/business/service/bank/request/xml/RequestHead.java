package com.newtouch.uctp.module.business.service.bank.request.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class RequestHead {

    /**
     * 交易码
     */
    @JacksonXmlProperty(localName = "tr_code")
    private String trCode = "1";

    /**
     * 企业代码
     */
    @JacksonXmlProperty(localName = "corp_no")
    private String corpNo;

    /**
     * 企业用户号
     */
    @JacksonXmlProperty(localName = "user_no")
    private String userNo;

    /**
     * 发起方序号
     */
    @JacksonXmlProperty(localName = "req_no")
    private String reqNo;

    /**
     * 交易日期
     */
    @JacksonXmlProperty(localName = "tr_acdt")
    private Long trAcdt;

    /**
     * 时间
     */
    @JacksonXmlProperty(localName = "tr_time")
    private Long trTime;

    /**
     * 原子交易数
     */
    @JacksonXmlProperty(localName = "atom_tr_count")
    private Long atomTrCount;

    /**
     * 渠道标志
     */
    @JacksonXmlProperty(localName = "channel")
    private String channel;

    /**
     * 保留字段
     */
    @JacksonXmlProperty(localName = "reserved")
    private String reserved;
}
