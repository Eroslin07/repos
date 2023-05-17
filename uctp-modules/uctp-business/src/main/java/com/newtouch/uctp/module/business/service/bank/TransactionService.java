package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.module.business.service.bank.request.BillNoticeRequestV1;
import com.newtouch.uctp.module.business.service.bank.request.CreateTrsCusAcRequestV1;
import com.newtouch.uctp.module.business.service.bank.request.ForwardGetTokenRequestV1;
import com.newtouch.uctp.module.business.service.bank.response.BillNoticeResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.CreateTrsCusAcResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.ForwardGetTokenResponseV1;

/**
 * 银行交易服务
 */
public interface TransactionService {



     CreateTrsCusAcResponseV1 getTrsCusAc(CreateTrsCusAcRequestV1 request);


     BillNoticeResponseV1 getBillNotice(BillNoticeRequestV1 request);


     ForwardGetTokenResponseV1 getToken(ForwardGetTokenRequestV1 request);
}
