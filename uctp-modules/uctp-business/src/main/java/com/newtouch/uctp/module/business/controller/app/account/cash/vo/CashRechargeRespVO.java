package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "账户保证金充值结果")
@Data
public class CashRechargeRespVO {

    private String result;

    @Schema(description = "交易方式 1：线上交易 字段result内容为转跳银行APP地址 \n2:线下交易 result字段无值")
    private Integer code;
}
