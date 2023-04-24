package com.newtouch.uctp.module.business.controller.app.contact;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class QYSContractVO {
    @Schema(description = "合同ID")
    private Long contractId;

    @Schema(description = "类型-收车或卖车（1：收车，2：卖车")
    private String type;

    @Schema(description = "文件访问url")
    private String url;

    @Schema(description = "车辆ID")
    private Long carId;
}
