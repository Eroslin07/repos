package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
* 合同信息  VO
*/
@Data
@ToString(callSuper = true)
public class AppContractarVO {


    @Schema(description = "车辆ID")
    private String carID;

    @Schema(description = "合同编号")
    private String contractID;

    @Schema(description = "合同名称")
    private String contractName;

    @Schema(description = "合同状态(0已签约 1已终止 2已作废)")
    private String status;

    @Schema(description = "合同url")
    private String url;

    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "合同路径")
    private String filePath;

    @Schema(description = "是否3方收款")
    private String collection;



}
