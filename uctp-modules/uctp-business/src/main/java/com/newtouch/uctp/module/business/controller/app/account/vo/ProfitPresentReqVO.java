package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "资金管理 - 利润提现申请")
@Data
public class ProfitPresentReqVO {
    @Schema(description = "商户账户号")
    @NotNull(message = "商户账户号不能为空")
    private String accountNo;
    @Schema(description = "提现金额（单位为：分）")
    @NotNull(message = "提现金额不能为空")
    @Min(value = 1, message = "提现金额必须大于0")
    private Long amount;
    @Schema(description = "发票文件清单")
    @NotNull(message = "发票不能为空")
    private List<ProfitPresentInvoiceReqVO> invoiceFiles;
}
