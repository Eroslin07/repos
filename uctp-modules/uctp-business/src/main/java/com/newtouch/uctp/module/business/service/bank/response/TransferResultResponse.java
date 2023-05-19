package com.newtouch.uctp.module.business.service.bank.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 转账交易结果查询响应
 */
@Data
public class TransferResultResponse {

    /**
     * 原流水号
     */
    @JacksonXmlProperty(localName = "ogl_serial_no")
    private String oglSerialNo;

    /**
     * 状态
     */
    @JacksonXmlProperty(localName = "stat")
    private String stat;

    /**
     * 错误信息
     */
    @JacksonXmlProperty(localName = "err_msg")
    private String errMsg;

    /**
     * 付款人账号
     */
    @JacksonXmlProperty(localName = "pay_acno")
    private String payAcno;

    /**
     * 付款行号
     */
    @JacksonXmlProperty(localName = "pay_bank_no")
    private String payBankNo;

    /**
     * 收款人账号
     */
    @JacksonXmlProperty(localName = "rcv_acno")
    private String rcvAcno;

    /**
     * 收款方行号
     */
    @JacksonXmlProperty(localName = "rcv_bank_no")
    private String rcvBankNo;

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
}
