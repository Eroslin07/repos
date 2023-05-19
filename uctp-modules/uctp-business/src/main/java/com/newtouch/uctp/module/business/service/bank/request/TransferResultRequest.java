package com.newtouch.uctp.module.business.service.bank.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账交易结果查询请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResultRequest {

    /**
     * 查询标志
     */
    @JacksonXmlProperty(localName = "query_flag")
    private String queryFlag;

    /**
     * 流水号
     */
    @JacksonXmlProperty(localName = "ogl_serial_no")
    private String oglSerialNo;
}
