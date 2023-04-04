package com.newtouch.uctp.module.business.controller.app.carinfodetails.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 车辆明细创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarInfoDetailsCreateReqVO extends CarInfoDetailsBaseVO {
}
