package com.newtouch.uctp.module.business.service.bank.impl;

import com.newtouch.uctp.module.business.service.bank.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class TransactionServiceImpl implements TransactionService {


    @Override
    public void noticePaymentResult() {

    }

    @Override
    public String nominalAccountCreate() {
        return null;
    }

    @Override
    public String outGold() {
        return null;
    }
}
