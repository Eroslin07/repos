package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;

/**
 * 流程需要的车辆过户数据
 * @author helong
 * @date 2023/4/23 18:00
 */
@Schema(description = "用户 APP - 流程车辆过户数据 VO")
@Data
@ToString(callSuper = true)
public class CarTransferInfoVO implements Serializable {
    @Schema(description = "车辆主表")
    private CarInfoDO carInfo;
    @Schema(description = "车辆副表")
    private CarInfoDetailsDO carInfoDetails;
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
    @Schema(description = "收车/卖车合同号，流程生成标题时使用")
    private String contractCode;
    @Schema(description = "收车/卖车合同及合同附件简要信息")
    private List<ContractApprovalShowVO> contractList = Lists.newArrayList();
    @Schema(description = "收车/卖车发票信息")
    private CarInvoiceInfoVO carInvoiceInfoVO;

}
