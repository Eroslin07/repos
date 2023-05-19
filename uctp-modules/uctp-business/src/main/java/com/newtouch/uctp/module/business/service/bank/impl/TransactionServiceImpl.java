package com.newtouch.uctp.module.business.service.bank.impl;

import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.*;
import com.newtouch.uctp.module.business.service.bank.request.xml.BankRequest;
import com.newtouch.uctp.module.business.service.bank.response.BillNoticeResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.CreateTrsCusAcResponseV1;
import com.newtouch.uctp.module.business.service.bank.response.ForwardGetTokenResponseV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    @Override
    public CreateTrsCusAcResponseV1 getTrsCusAc(CreateTrsCusAcRequestV1 request) {
        return null;
    }

    @Override
    public BillNoticeResponseV1 getBillNotice(BillNoticeRequestV1 request) {
        return null;
    }

    @Override
    public ForwardGetTokenResponseV1 getToken(ForwardGetTokenRequestV1 request) {
        return null;
    }

    @Override
    public boolean outTransfer(BankRequest<OutTransferRequest> request) {
        return false;
    }

    @Override
    public boolean getTransferResult(BankRequest<TransferResultRequest> request) {
        return false;
    }
}
