package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 车辆卡片数据
 */
@Schema(description = "用户 APP - 车辆卡片 VO")
@Data
@ToString(callSuper = true)
public class AppCarInfoCardRespVO implements Serializable {
    @Schema(description = "车辆主表")
    private CarInfoDO carInfo;
    @Schema(description = "车辆副表")
    private CarInfoDetailsDO carInfoDetails;
    @Schema(description = "有效合同")
    private List<APPContractCardVO> contractCardVOS;
    @Schema(description = "无效合同")
    private List<APPContractCardVO> contractCardNOS;
    @Schema(description = "资金信息")
    private AppCarInfoAmountRespVO appCarInfoAmountRespVO;
    @Schema(description = "车辆图片")
    private List<AppSimpleFileVO> fileA = Lists.newArrayList();
    @Schema(description = "车辆行驶证图片")
    private List<AppSimpleFileVO> fileB = Lists.newArrayList();
    @Schema(description = "车辆机动车登记证书图片")
    private List<AppSimpleFileVO> fileC = Lists.newArrayList();
    @Schema(description = "卖家身份证图片(收车时)")
    private List<AppSimpleFileVO> fileD = Lists.newArrayList();
    @Schema(description = "买家身份证图片(卖车时)")
    private List<AppSimpleFileVO> fileE = Lists.newArrayList();
    @Schema(description = "检测报告")
    private List<AppSimpleFileVO> fileF = Lists.newArrayList();
}
