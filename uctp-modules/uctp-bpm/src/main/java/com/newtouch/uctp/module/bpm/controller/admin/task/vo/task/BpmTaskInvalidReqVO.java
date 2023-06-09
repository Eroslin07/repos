package com.newtouch.uctp.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author helong
 * @date 2023/4/11 11:38
 */
@Schema(description = "管理后台 - 作废流程任务的 Request VO")
@Data
public class BpmTaskInvalidReqVO {
    @Schema(description = "任务编号", required = true, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String taskId;

    @Schema(description = "审批意见", required = true, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;
}
