package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationRespVO;
import com.newtouch.uctp.module.business.enums.bank.NotificationResultCode;
import com.newtouch.uctp.module.business.enums.bank.NotificationTranCode;
import com.newtouch.uctp.module.business.enums.bank.SPDBBankTrans;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/notification")
public class BankNotificationController {

    @PostMapping("/deposits")
    @Operation(summary = "银行入金回调接口")
    public DepositsNotificationRespVO depositsNotification(@RequestBody DepositsNotificationReqVO notificationReqVO) {
        log.info("入金通知请求报文: {}", notificationReqVO);
        DepositsNotificationRespVO respVO = new DepositsNotificationRespVO();
        LocalDateTime now = LocalDateTime.now();
        respVO.setResultHead(DepositsNotificationRespVO.ResultHead.builder()
                .noticeDate(SPDBBankTrans.TRAN_DATE_FORMAT.get(now))
                .noticeTime(SPDBBankTrans.TRAN_TIME_FORMAT.get(now))
                .noticeSerNo(notificationReqVO.getPostBeanHead().getNoticeSerNo())
                .noticeType(NotificationTranCode.NOTICE_TYPE.getCode())
                .statusCode(NotificationResultCode.RESULT_STATUS_CODE.getCode())
                .rspMessage("").build());

        respVO.setData(DepositsNotificationRespVO.ResultBody.builder()
                .returnCode(NotificationResultCode.RESULT_RETURN_CODE.getCode())
                .returnMessage("")
                .build());


        log.info("入金通知请求报文: {}", respVO);
        return respVO;
    }
}
