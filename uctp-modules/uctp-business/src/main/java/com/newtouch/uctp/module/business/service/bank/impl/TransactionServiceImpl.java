package com.newtouch.uctp.module.business.service.bank.impl;

import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.BillNoticeRequestV1;
import com.newtouch.uctp.module.business.service.bank.request.CreateTrsCusAcRequestV1;
import com.newtouch.uctp.module.business.service.bank.request.ForwardGetTokenRequestV1;
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
}
