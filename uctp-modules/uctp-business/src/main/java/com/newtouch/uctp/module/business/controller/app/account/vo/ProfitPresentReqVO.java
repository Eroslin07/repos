package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "资金管理 - 利润提现申请")
@Data
public class ProfitPresentReqVO {
    @Schema(description = "商户银行卡ID")
    private Long merchantBankId;
    @Schema(description = "提现金额（单位为：分）")
    private Integer amount;
    @Schema(description = "发票ID")
    private List<String> invoiceIds;
}
