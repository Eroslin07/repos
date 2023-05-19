package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.module.business.service.bank.PosAPIService;
import com.newtouch.uctp.module.business.service.bank.request.PosNonPayOrderRequest;
import com.newtouch.uctp.module.business.service.bank.request.PosPayNoticeRequest;
import com.newtouch.uctp.module.business.service.bank.response.PosNonPayOrderResponse;
import com.newtouch.uctp.module.business.service.bank.response.PosPayNoticeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/merchant")
public class BankController {

    @Resource
    private PosAPIService posAPIService;

    @PostMapping("/pay/orders")
    public List<PosNonPayOrderResponse> getNonPayOrders(PosNonPayOrderRequest request) {
        return posAPIService.getNonPayOrders(request);
    }

    @PostMapping("/pay/notice")
    public PosPayNoticeResponse payNotice(PosPayNoticeRequest request) {
        return posAPIService.payNotice(request);
    }
}
