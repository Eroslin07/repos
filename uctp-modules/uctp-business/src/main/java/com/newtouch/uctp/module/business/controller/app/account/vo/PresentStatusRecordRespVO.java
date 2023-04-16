package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "资金管理 - 提现状态记录")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresentStatusRecordRespVO {
    @Schema(description = "标识")
    private Long presentNo;

    @Schema(description = "状态变更日期，格式：yyyy-MM-dd")
    private String occurredTime;

    @Schema(description = "状态")
    private String status;
}
