package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Schema(description = "用户 APP - 首页卖车 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppSellCarInfoPageRespVO extends AppCarInfoBaseVO {
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "车辆主表id")
    private String id;
    @Schema(description = "甲方(卖方)")
    private String firstParty;
    @Schema(description = "乙方(买方)")
    private String secondParty;
    @Schema(description = "里程数")
    private String mileage;
    @Schema(description = "图片地址")
    private String url;

}
