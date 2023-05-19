package com.newtouch.uctp.module.business.service.bank.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class OutTransferRequest {

    /**
     * 付款人账号
     */
    @JacksonXmlProperty(localName = "pay_acno")
    private String payAcno;

    /**
     * 付款人户名
     */
    @JacksonXmlProperty(localName = "pay_acname")
    private String payAcname;

    /**
     * 收款方行名
     */
    @JacksonXmlProperty(localName = "rcv_bank_name")
    private String rcvBankName;

    /**
     * 收款人账号
     */
    @JacksonXmlProperty(localName = "rcv_acno")
    private String rcvAcno;

    /**
     * 收款人户名
     */
    @JacksonXmlProperty(localName = "rcv_acname")
    private String rcvAcname;

    /**
     * 收款方交换号
     */
    @JacksonXmlProperty(localName = "rcv_exg_code")
    private String rcvExgCode;

    /**
     * 收款方联行号
     */
    @JacksonXmlProperty(localName = "rcv_bank_no")
    private String rcvBankNo;

    /**
     * 币种
     */
    @JacksonXmlProperty(localName = "cur_code")
    private String curCode;

    /**
     * 金额
     */
    @JacksonXmlProperty(localName = "amt")
    private String amt;

    /**
     * 企业凭证编号
     */
    @JacksonXmlProperty(localName = "cert_no")
    private String certNo;

    /**
     * 附言
     */
    @JacksonXmlProperty(localName = "summary")
    private String summary;

    /**
     * 银行标志
     */
    @JacksonXmlProperty(localName = "bank_flag")
    private String bankFlag;

    /**
     * 同城异地标志
     */
    @JacksonXmlProperty(localName = "area_flag")
    private String areaFlag;
}
