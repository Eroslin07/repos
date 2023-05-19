package com.newtouch.uctp.module.business.service.bank.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosPayNoticeResponse {

    /**
     * 接收状态
     */
    private String code;

    /**
     * 消息
     */
    private String message;
}
