package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class CarDetailRespVO {
    @Schema(description = "车辆信息")
    private AppCarInfoAndDetailVO carInfoAndDetailVO;
    @Schema(description = "合同信息")
    private List<AppContractarVO> contractarVO;
    @Schema(description = "资金信息")
    private AppCarCostVO carCostVO;
    @Schema(description = "发票信息")
    private List<AppCarInvoiceVo> carInvoiceVO;


}
