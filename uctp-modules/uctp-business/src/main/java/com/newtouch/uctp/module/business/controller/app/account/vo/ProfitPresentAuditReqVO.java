package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "资金管理 - 利润提现审核")
@Data
public class ProfitPresentAuditReqVO {
    @Schema(description = "利润提现id")
    @NotNull(message = "利润提现id不能为空")
    private String profitId;
    @Schema(description = "审核意见，1：处理中，2：审核通过，3：审核退回")
    @NotNull(message = "商户银行卡ID不能为空")
    private int auditOpinion;
}
