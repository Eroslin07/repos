package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "商户保证金APP充值跳转地址响应")
@Data
public class AppTransferRespVO {

    @Schema(description = "iOS跳转的地址")
    private String iosAddress;

    @Schema(description = "android跳转地址")
    private String androidAddress;

    @Schema(description = "h5跳转APP的地址")
    private String urlAddress;
}
