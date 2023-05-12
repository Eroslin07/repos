package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationRespVO;
import com.newtouch.uctp.module.business.enums.bank.NotificationResultCode;
import com.newtouch.uctp.module.business.enums.bank.NotificationTranCode;
import com.newtouch.uctp.module.business.enums.bank.SPDBBankTrans;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/notification")
public class BankNotificationController {

    @Resource
    private TransactionService transactionService;

    @PostMapping("/deposits")
    @Operation(summary = "银行入金回调接口")
    public DepositsNotificationRespVO depositsNotification(@RequestBody DepositsNotificationReqVO notificationReqVO) {
        log.info("入金通知请求报文: {}", notificationReqVO);

        return transactionService.noticePaymentResult(notificationReqVO);
    }
}
