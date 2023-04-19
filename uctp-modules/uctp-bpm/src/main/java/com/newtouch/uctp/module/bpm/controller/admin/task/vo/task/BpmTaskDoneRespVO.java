package com.newtouch.uctp.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务的已办分页项 Response VO")
@Data
public class BpmTaskDoneRespVO {
    private String businessKey;

    private String procInstId;

    private String procDefId;

    private String serialNo;

    private String title;

    private String merchantName;

    private String startUserName;

    private LocalDateTime submitTime;

    private LocalDateTime arrivalTime;

    private String assigneeUserId;

    private String assigneeUserName;
}
