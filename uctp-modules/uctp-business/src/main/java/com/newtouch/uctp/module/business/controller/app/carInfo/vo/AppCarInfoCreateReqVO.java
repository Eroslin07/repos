package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "用户 APP - 车辆主表创建 Request VO")
@Data
@ToString(callSuper = true)
public class AppCarInfoCreateReqVO{

    @Schema(description = "车辆图片url")
    private List<String>  carUrl;

    @Schema(description = "行驶证url")
    private List<String>  drivingLicenseUrl;

    @Schema(description = "行驶证编号")
    private String drivingLicense;

    @Schema(description = "发动机编号")
    private String engineNum;

    @Schema(description = "车架号")
    private String vin;

    @Schema(description = "首次登记时间")
    private LocalDateTime firstRegistDate;

    @Schema(description = "登记证书url")
    private List<String>  certificateUrl;


    @Schema(description = "里程数")
    private BigDecimal mileage;

    @Schema(description = "车辆品牌")
    private String brand;

    @Schema(description = "车辆年份")
    private String year;

    @Schema(description = "车辆型号")
    private String model;

    @Schema(description = "收车金额")
    private BigDecimal vehicleReceiptAmount;

    @Schema(description = "备注（特别约定）")
    private String remarks;

    @Schema(description = "收车方式")
    private String vehicleReceiptType;


}
