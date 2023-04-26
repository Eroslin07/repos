package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "资金管理 - 月度费用")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitCostMonthRespVO extends ProfitRespVO {
    @Schema(description = "月度，格式yyyyMM，如：202301")
    private String month;
    @Schema(description = "月度服务费总额")
    private Long costTotalAmount;
    @Schema(description = "月度税费总额")
    private Long taxTotalAmount;
}
