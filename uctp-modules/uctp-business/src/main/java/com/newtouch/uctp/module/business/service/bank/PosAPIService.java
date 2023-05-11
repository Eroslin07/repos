package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.module.business.service.bank.request.PosPaymentRequest;

public interface PosAPIService {

    /**
     * POS机支付消息推送
     *
     * @return
     */
    String posPaymentPush(PosPaymentRequest request);
}
