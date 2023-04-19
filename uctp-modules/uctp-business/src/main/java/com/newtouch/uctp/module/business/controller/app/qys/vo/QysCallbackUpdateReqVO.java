package com.newtouch.uctp.module.business.controller.app.qys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description ="管理后台 - 契约锁回调日志更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysCallbackUpdateReqVO extends QysCallbackBaseVO {

    @Schema(description = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

}
