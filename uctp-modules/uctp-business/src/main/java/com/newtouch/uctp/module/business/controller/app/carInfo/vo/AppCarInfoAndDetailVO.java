package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
* 车辆明细表  VO
*/
@Data
@ToString(callSuper = true)
public class AppCarInfoAndDetailVO {


    @Schema(description = "车辆ID")
    private String id;

    @Schema(description = "里程数")
    private String MILEAGE;

    @Schema(description = "车辆品牌")
    private String BRAND;

    @Schema(description = "车辆年份")
    private String YEAR;

    @Schema(description = "车辆型号")
    private String MODEL;

    @Schema(description = "车架号")
    private String VIN;

    @Schema(description = "收车时间")
    private String createTime;
/*
    @Schema(description = "机动车登记证")
    private String certificateNo;

    @Schema(description = "行驶证")
    private String drivingLicense;*/

    @Schema(description = "出售状态")
    private String salesStatus;

    @Schema(description = "检测状态")
    private String checkStatus;

    @Schema(description = "车辆图片")
    private List<String> carPic;


    @Schema(description = "在库时间")
    private String inStockTime;

    @Schema(description = "收款方式")
    private String remitType;

    @Schema(description = "付款方式")
    private String payType;

    @Schema(description = "首次上牌时间")
    private String firsRegistration;

    @Schema(description = "首次登记时间")
    private String firstRegistDate;

    @Schema(description = "经办人")
    private String operator;

    @Schema(description = "发动机编号")
    private String engineNum;


}
