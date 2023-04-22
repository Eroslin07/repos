package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "资金管理 - 费用查询")
@Data
public class ProfitCostQueryReqVO {
    @Schema(description = "商户账户号")
    @NotNull(message = "商户账户号不能为空")
    private String accountNo;
    @Schema(description = "查询季度，格式：yyyy+Qn，如：2023Q1")
    @Pattern(regexp = "^\\d{4}Q[1-4]$", message = "季度格式不正确")
    @NotNull(message = "季度不能为空")
    private String quarter;
}
