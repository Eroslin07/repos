package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class CarDCVo {
    @Schema(description = "获取下载文件路径的id")
    private Long longId;
    @Schema(description = "车辆ID")
    private String id;
    @Schema(description = "驾驶证编号")
    private String drivingLicense;
    @Schema(description = "行驶证编号")
    private String certificateNo;
    @Schema(description = "合同编号")
    private String contractID;

}
