package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

@Data
public class PosPaymentRequest {

    private String merchantId;

    private String amount;

    private String devSn;
}
