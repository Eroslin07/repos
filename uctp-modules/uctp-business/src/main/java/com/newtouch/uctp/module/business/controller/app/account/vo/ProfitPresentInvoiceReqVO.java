package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "资金管理 - 利润提现申请中的发票")
@Data
public class ProfitPresentInvoiceReqVO {
    @Schema(description = "发票文件ID")
    @NotNull(message = "发票ID不能为空")
    private String fileId;
    @Schema(description = "发票文件URL")
    @NotNull(message = "发票文件URL不能为空")
    private String fileUrl;
}
