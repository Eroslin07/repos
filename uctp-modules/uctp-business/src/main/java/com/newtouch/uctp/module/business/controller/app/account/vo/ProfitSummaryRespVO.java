package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资金管理 - 利润概要")
@Data
public class ProfitSummaryRespVO {
    @Schema(description = "利润余额（单位为：分）")
    private Integer profit;
    @Schema(description = "利润-冻结余额（单位为：分）")
    private Integer freezeProfit;
    @Schema(description = "待回填保证金（单位为：分）")
    private String cash_back;
}
