package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资金管理 - 利润明细")
@Data
public class ProfitRespVO {
    @Schema(description = "交易类型")
    private String tradeType;
    @Schema(description = "提现状态")
    private String presentState;
    @Schema(description = "利润金额")
    private String amount;
    @Schema(description = "发生日期")
    private String tradeDate;
    @Schema(description = "收支类型，revenue：收入，expenditure：支付")
    private String revenueAndExpenditureType;
}
