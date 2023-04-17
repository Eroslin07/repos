package com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 契约锁更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysConfigUpdateReqVO extends QysConfigBaseVO {

   @Schema(description = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

}
