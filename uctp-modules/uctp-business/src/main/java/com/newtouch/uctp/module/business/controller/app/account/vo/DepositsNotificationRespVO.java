package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "入金通知响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositsNotificationRespVO {

    private ResultBody data;

    private ResultHead resultHead;

    @Data
    @Builder
    public static class ResultBody {
        private String returnCode;
        private String returnMessage;
    }

    @Data
    @Builder
    public static class ResultHead {

        private String noticeDate;
        private String noticeTime;
        private String noticeType;
        private String noticeSerNo;
        private String statusCode;
        private String rspMessage;
    }
}
