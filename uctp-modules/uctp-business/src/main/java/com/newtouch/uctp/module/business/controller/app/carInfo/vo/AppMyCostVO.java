package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
* 我的费用  VO
*/
@Data
@ToString(callSuper = true)
public class AppMyCostVO {


    @Schema(description = "车辆ID")
    private String id;

    @Schema(description = "车辆品牌")
    private String BRAND;

    @Schema(description = "车辆年份")
    private String YEAR;

    @Schema(description = "车辆型号")
    private String MODEL;

    @Schema(description = "车架号")
    private String VIN;

    @Schema(description = "创办人")
    private String creator;

    @Schema(description = "出售状态")
    private String salesStatus;

    @Schema(description = "利润")
    private String profit;

    @Schema(description = "收车金额")
    private String vehicleReceiptAmount;

    @Schema(description = "卖车金额")
    private String carSalesAmount;



/*    @Schema(description = "支付状态")
    private String  payType;*/



}
