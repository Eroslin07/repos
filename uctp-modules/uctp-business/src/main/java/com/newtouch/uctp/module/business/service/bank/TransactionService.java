package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.module.business.service.bank.request.*;
import com.newtouch.uctp.module.business.service.bank.request.xml.BankRequest;
import com.newtouch.uctp.module.business.service.bank.response.BillNoticeResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.CreateTrsCusAcResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.ForwardGetTokenResponseV1;

/**
 * 银行交易服务
 */
public interface TransactionService {


    /**
     * 获取收款识别码
     *
     * @param request
     * @return
     */
    CreateTrsCusAcResponseV1 getTrsCusAc(CreateTrsCusAcRequestV1 request);

    /**
     * 转账支付结果通知
     *
     * @param request
     * @return
     */
    BillNoticeResponseV1 getBillNotice(BillNoticeRequestV1 request);

    /**
     * 获取企业手机银行转账Token
     *
     * @param request
     * @return
     */
    ForwardGetTokenResponseV1 getToken(ForwardGetTokenRequestV1 request);

    /**
     * 对外转账交易
     */
    boolean outTransfer(BankRequest<OutTransferRequest> request);

    /**
     * 转账交易结果查询
     */
    boolean getTransferResult(BankRequest<TransferResultRequest> request);
}
