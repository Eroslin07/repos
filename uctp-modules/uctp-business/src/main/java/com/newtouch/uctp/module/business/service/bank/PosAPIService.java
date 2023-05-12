package com.newtouch.uctp.module.business.service.bank;

import com.newland.pospp.iot.lib.exceptions.BusinessException;
import com.newland.pospp.iot.lib.exceptions.NetworkException;
import com.newtouch.uctp.module.business.service.bank.request.PosPaymentRequest;

public interface PosAPIService {

    /**
     * POS机支付消息推送
     *
     * @return
     */
    String posPaymentPush(PosPaymentRequest request);


    /**
     * 根据posName获取POS授权码
     *
     * @param posName
     * @return
     */
    String getAuthCode(String posName) throws BusinessException, NetworkException;
}
