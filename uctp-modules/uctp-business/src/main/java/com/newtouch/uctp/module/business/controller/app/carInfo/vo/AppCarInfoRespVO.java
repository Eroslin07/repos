package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Schema(description = "用户 APP - 车辆主表 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCarInfoRespVO extends AppCarInfoBaseVO {

    @Schema(description = "车辆主表id", required = true)
    private Long id;

    @Schema(description = "创建时间", required = true)
    private Date createTime;

}
