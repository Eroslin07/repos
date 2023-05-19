package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.module.business.service.bank.request.PosNonPayOrderRequest;
import com.newtouch.uctp.module.business.service.bank.request.PosPayNoticeRequest;
import com.newtouch.uctp.module.business.service.bank.response.PosNonPayOrderResponse;
import com.newtouch.uctp.module.business.service.bank.response.PosPayNoticeResponse;

import java.util.List;

public interface PosAPIService {

    List<PosNonPayOrderResponse> getNonPayOrders(PosNonPayOrderRequest request);

    PosPayNoticeResponse payNotice(PosPayNoticeRequest request);
}
