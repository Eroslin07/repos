package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "资金管理 - 提现状态记录")
@Data
public class PresentStatusRecordRespVO {

    @Schema(description = "状态变更日期，格式：yyyy-MM-dd")
    private String statusDate;
    @Schema(description = "状态")
    private String status;
}
