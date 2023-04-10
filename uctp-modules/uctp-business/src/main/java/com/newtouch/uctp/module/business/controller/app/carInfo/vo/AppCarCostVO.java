package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
* 车辆费用  VO
*/
@Data
@ToString(callSuper = true)
public class AppCarCostVO {


    @Schema(description = "车辆ID")
    private String mainID;

    @Schema(description = "收车金额")
    private String vehicleReceiptAmount;

    @Schema(description = "检测服务费")
    private String testingFee;

    @Schema(description = "运营服务费")
    private String operationFee;

    @Schema(description = "过户服务费")
    private String transferFee;

    @Schema(description = "增值税费用")
    private String taxation;

    @Schema(description = "杂税费用")
    private String otherTaxation;

    @Schema(description = "卖车金额")
    private String carSalesAmount;

    @Schema(description = "利润")
    private String profit;

    @Schema(description = "总支出")
    private String  costCount;

    @Schema(description = "支付状态")
    private String  payType;



}
