package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "资金管理 - 利润明细")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfitRespVO {
    @Schema(description = "交易类型")
    private String tradeType;
    @Schema(description = "交易类型中文名称")
    private String tradeTypeText;
    @Schema(description = "提现状态")
    private String presentState;
    @Schema(description = "提现状态中文名称")
    private String presentStateText;
    @Schema(description = "利润金额（单位为：分）")
    private Integer amount;
    @Schema(description = "发生日期，格式：yyyy-MM-dd")
    private String tradeDate;
    @Schema(description = "损益类型")
    private String profitLossType;
    @Schema(description = "损益类型中文名称")
    private String profitLossTypeText;
}
