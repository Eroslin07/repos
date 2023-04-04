package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆主表 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AppCarInfoBaseVO {
    @Schema(description = "车架号")
    private String vin;

    @Schema(description = "车辆品牌")
    private String brand;

    @Schema(description = "车辆年份")
    private String year;

    @Schema(description = "车辆型号")
    private String model;

    @Schema(description = "发动机编号")
    private String engineNum;

    @Schema(description = "收车金额")
    private BigDecimal vehicleReceiptAmount;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "售卖状态")
    private Integer salesStatus;

    @Schema(description = "检测状态")
    private Integer checkStatus;

    @Schema(description = "汽车明细id")
    private String carDetailId;

    @Schema(description = "商户id")
    private String businessId;
}
