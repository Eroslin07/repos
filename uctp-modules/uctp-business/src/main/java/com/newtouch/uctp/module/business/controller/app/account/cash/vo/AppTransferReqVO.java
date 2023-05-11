package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "商户保证金APP充值跳转地址请求")
@Data
public class AppTransferReqVO {

    @Schema(description = "商户虚拟账户号")
    private String accountNo;

    @Schema(description = "转账金额 保留小数点两位00.00")
    private String amount;
}
