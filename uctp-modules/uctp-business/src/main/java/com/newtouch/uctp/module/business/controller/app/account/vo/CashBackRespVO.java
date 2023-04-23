package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "资金管理 - 待回填保证金明细")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashBackRespVO {
    @Schema(description = "id")
    private String id;
    @Schema(description = "账户号")
    private String accountNo;
    @Schema(description = "类型")
    private String type;
    @Schema(description = "类型中文名称")
    private String typeText;
    @Schema(description = "交易去向")
    private String tradeTo;
    @Schema(description = "交易去向中文名称")
    private String tradeToText;
    @Schema(description = "交易合同号")
    private String contractNo;
    @Schema(description = "金额")
    private Integer amount;
    @Schema(description = "发生时间")
    private String occurredTime;
}
