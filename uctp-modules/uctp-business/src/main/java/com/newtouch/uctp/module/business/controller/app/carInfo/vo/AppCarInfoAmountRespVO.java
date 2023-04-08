package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "用户 APP - 卖车详情的明细费用 VO")
@Data
@ToString(callSuper = true)
public class AppCarInfoAmountRespVO{
    //税配置100，服务费配置200，然后合计费用300；利润=卖车金额-收车金额-税-服务费
    //杂税不配置税率，增值税配置0.5%。税费等于卖车合同*税率
    @Schema(description = "收车金额")
    private BigDecimal received;
    @Schema(description = "卖车金额")
    private BigDecimal sell;
    @Schema(description = "过户服务费")
    private BigDecimal transfer = new BigDecimal("200");
    @Schema(description = "运营服务费")
    private BigDecimal operating = new BigDecimal("200");
    @Schema(description = "增值税费用")
    private BigDecimal vat = new BigDecimal("100");
    @Schema(description = "杂税费用")
    private BigDecimal tax = new BigDecimal("100");
    @Schema(description = "合计费用")
    private BigDecimal total = new BigDecimal("300");
    @Schema(description = "利润")
    private BigDecimal profit;

    public void calculation(BigDecimal received,BigDecimal sell){
        this.received = received;
        this.sell = sell;
        this.profit = sell.subtract(received).subtract(this.vat).subtract(this.tax);
    }
}
