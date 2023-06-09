package com.newtouch.uctp.module.business.service.bank.impl;

import com.newtouch.uctp.module.business.service.bank.PosAPIService;
import com.newtouch.uctp.module.business.service.bank.request.PosNonPayOrderRequest;
import com.newtouch.uctp.module.business.service.bank.request.PosPayNoticeRequest;
import com.newtouch.uctp.module.business.service.bank.response.PosNonPayOrderResponse;
import com.newtouch.uctp.module.business.service.bank.response.PosPayNoticeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PosAPIServiceImpl implements PosAPIService {


    @Override
    public List<PosNonPayOrderResponse> getNonPayOrders(PosNonPayOrderRequest request) {
        List<PosNonPayOrderResponse> list = new ArrayList<>();
        list.add(PosNonPayOrderResponse.builder().orderNo("1234567899").amount(0.01D).vin("1234567890987654321").build());
        return list;
    }

    @Override
    public PosPayNoticeResponse payNotice(PosPayNoticeRequest request) {
        return PosPayNoticeResponse.builder().code("0").message("").build();
    }
}
