package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "入金通知请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositsNotificationReqVO {

    private PostBeanHead postBeanHead;

    private PostBeanBody data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostBeanBody {
        private String acctBankName;
        private String acctBankNo;
        private String acctName;
        private String acctNo;
        private String amtKind;
        private String coreSerNo;
        private String coreTransDate;
        private String coreTransTime;
        private String depSerNo;
        private String merchName;
        private String merchNo;
        private String parentAcctName;
        private String parentAcctNo;
        private String remark1;
        private String secuName;
        private String secuNo;
        private String subAcctBal;
        private String transAmt;
        private String transDate;
        private String transTime;
        private String zoneCode;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostBeanHead {

        private String noticeDate;
        private String noticeTime;
        private String noticeType;
        private String noticeSerNo;
    }
}
