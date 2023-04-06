package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Schema(description = "用户 APP - 首页车辆状态分类统计 Response VO")
@Data
@ToString(callSuper = true)
public class AppHomeCountRespVO {
    @Schema(description = "车辆状态")
    private Integer status;
    @Schema(description = "车辆记数")
    private Integer num;

}
