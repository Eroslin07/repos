package com.newtouch.uctp.module.business.service.bank.request.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "ap")
public class BankRequest<T> {

    @JacksonXmlProperty(localName = "head")
    private RequestHead head;

    @JacksonXmlProperty(localName = "body")
    private T body;
}
