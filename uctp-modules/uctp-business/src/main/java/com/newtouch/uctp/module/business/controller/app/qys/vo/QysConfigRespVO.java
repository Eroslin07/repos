package com.newtouch.uctp.module.business.controller.app.qys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 契约锁 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysConfigRespVO extends QysConfigBaseVO {

    @Schema(description = "id", required = true)
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
