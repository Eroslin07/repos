package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "资金管理 - 利润提现审核")
@Data
public class ProfitPresentAuditReqVO {
    @Schema(description = "流程业务key")
    @NotNull(message = "流程业务key不能为空")
    private String businessKey;
    @Schema(description = "审核意见，1：审核通过，2：审核退回")
    @NotNull(message = "审核意见不能为空")
    private int auditOpinion;
}
