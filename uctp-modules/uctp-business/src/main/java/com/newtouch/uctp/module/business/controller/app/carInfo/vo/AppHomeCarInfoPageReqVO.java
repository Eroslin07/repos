package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHomeCarInfoPageReqVO extends PageParam {
    @Schema(description = "车架号")
    private String vin;

    @Schema(description = "车辆品牌")
    private String brand;

    @Schema(description = "售卖状态")
    private Integer salesStatus;

    @Schema(description = "检测状态")
    private Integer checkStatus;

    @Schema(description = "甲方(卖方)")
    private String firstParty;
    @Schema(description = "乙方(买方)")
    private String secondParty;
}
