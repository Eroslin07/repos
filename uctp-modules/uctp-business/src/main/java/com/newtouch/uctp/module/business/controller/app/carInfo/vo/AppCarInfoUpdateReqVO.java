package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
@Schema(description = "管理后台 - 车辆更新 Request VO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class AppCarInfoUpdateReqVO extends AppCarInfoBaseVO {
    @Schema(description = "车辆主表id", required = true)
    @NotNull(message = "车辆主表id不能为空")
    private Long id;
}
