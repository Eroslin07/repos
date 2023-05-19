package com.newtouch.uctp.module.business.service.bank.response.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.newtouch.uctp.module.business.service.bank.request.xml.RequestHead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "ap")
public class BankResponse<T> {

    @JacksonXmlProperty(localName = "head")
    private RequestHead head;

    @JacksonXmlProperty(localName = "body")
    private T body;
}
