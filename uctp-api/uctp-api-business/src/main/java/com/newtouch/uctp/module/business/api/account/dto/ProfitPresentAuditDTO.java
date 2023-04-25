package com.newtouch.uctp.module.business.api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资金管理 - 利润提现审核数据传输对象")
@Data
public class ProfitPresentAuditDTO {
    @Schema(description = "利润提现id")
    private String profitId;
    @Schema(description = "审核意见，1：处理中，2：审核通过，3：审核退回")
    private int auditOpinion;
}
