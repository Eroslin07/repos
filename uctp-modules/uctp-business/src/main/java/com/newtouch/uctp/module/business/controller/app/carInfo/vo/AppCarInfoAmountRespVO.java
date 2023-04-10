package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "用户 APP - 卖车详情的明细费用 VO")
@Data
@ToString(callSuper = true)
public class AppCarInfoAmountRespVO{
    @Schema(description = "收车金额")
    private BigDecimal vehicleReceiptAmount;
    @Schema(description = "卖车金额")
    private BigDecimal sellAmount;
    @Schema(description = "过户服务费(卖家)")
    private BigDecimal transferSell;
    @Schema(description = "过户服务费(买家)")
    private BigDecimal transferBuy;
    @Schema(description = "运营服务费")
    private BigDecimal operation;
    @Schema(description = "增值税费用")
    private BigDecimal vat;
    @Schema(description = "杂税费用(暂未使用)")
    private BigDecimal tax;
    @Schema(description = "合计费用")
    private BigDecimal total;
    @Schema(description = "利润")
    private BigDecimal profit;

}
