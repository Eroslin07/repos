package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newtouch.uctp.module.business.dal.dataobject.ProceduresAndSpareParts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 车辆主表创建 Request VO")
@Data
@ToString(callSuper = true)
public class AppCarInfoCreateReqVO{

    @Schema(description = "商户id")
    private Long deptId;

    @Schema(description = "租户id")
    private Long tenantId;

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
    private String firstRegistDate;

    @Schema(description = "车牌号")
    private String plateNum;

    @Schema(description = "运营性质（使用性质）")
    private String natureOfOperat;

    @Schema(description = "车辆品牌")
    private String brand;

    @Schema(description = "车辆类型")
    private String carType;

    @Schema(description = "品牌型号")
    private String brandType;

    @Schema(description = "品牌/车型")
    private String model;

    @Schema(description = "车型id")
    private String modelId;

    @Schema(description = "登记证书url")
    private List<String>  certificateUrl;

    @Schema(description = "登记证号")
    private String certificateNo;

    @Schema(description = "颜色")
    private String colour;

    @Schema(description = "里程数")
    private BigDecimal mileage;


    @Schema(description = "备注（特别约定）")
    private String remarks;

    @Schema(description = "使用年限至")
    private String scrapDate;

    @Schema(description = "年检签证有效期至")
    private String annualInspectionDate;

    @Schema(description = "保险险种")
    private String insurance;

    @Schema(description = "保险期至")
    private String insuranceEndData;


    /**
     * 车辆手续及备件
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProceduresAndSpareParts proceduresAndSpareParts;
}
