package com.newtouch.uctp.module.business.controller.app.carinfodetails.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 车辆明细 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarInfoDetailsRespVO extends CarInfoDetailsBaseVO{

    @Schema(description =  "id", required = true)
    private String id;

    @Schema(description =  "创建时间")
    private LocalDateTime createTime;
}
