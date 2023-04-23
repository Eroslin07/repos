package com.newtouch.uctp.module.bpm.service.car.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDetailsDO;

/**
 * 流程需要的车辆数据
 */
@Schema(description = "用户 APP - 流程车辆数据 VO")
@Data
@ToString(callSuper = true)
public class AppBpmCarInfoRespDTO implements Serializable {
    @Schema(description = "车辆主表")
    private CarInfoDO carInfo;
    @Schema(description = "车辆副表")
    private CarInfoDetailsDO carInfoDetails;
    @Schema(description = "车辆图片")
    private List<AppSimpleFileDTO> fileA = Lists.newArrayList();
    @Schema(description = "车辆行驶证图片")
    private List<AppSimpleFileDTO> fileB = Lists.newArrayList();
    @Schema(description = "车辆机动车登记证书图片")
    private List<AppSimpleFileDTO> fileC = Lists.newArrayList();
    @Schema(description = "卖家身份证图片(收车时)")
    private List<AppSimpleFileDTO> fileD = Lists.newArrayList();
    @Schema(description = "买家身份证图片(卖车时)")
    private List<AppSimpleFileDTO> fileE = Lists.newArrayList();
}
