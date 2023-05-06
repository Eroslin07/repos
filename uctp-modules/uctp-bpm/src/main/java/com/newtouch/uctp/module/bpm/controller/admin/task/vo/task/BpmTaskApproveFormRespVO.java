package com.newtouch.uctp.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author helong
 * @date 2023/4/12 15:58
 */
@Schema(description = "管理后台 - 待办打开表单的基础通用信息 Response VO")
@Data
@ToString(callSuper = true)
public class BpmTaskApproveFormRespVO {
    @Schema(description = "任务ID")
    private String taskId;
    @Schema(description = "流程定义ID")
    private String procDefId;
    @Schema(description = "流程实例ID")
    private String procInstId;
    @Schema(description = "业务ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long businessKey;
    @Schema(description = "业务类型")
    private String busiType;
    @Schema(description = "申请单号")
    private String serialNo;
    @Schema(description = "标题（事项）")
    private String title;
    @Schema(description = "节点ID")
    private String nodeId;
    @Schema(description = "节点名称")
    private String nodeName;
    @Schema(description = "前端路由组件地址")
    private String componentAddress;
    @Schema(description = "变量实例")
    //@JsonSerialize(using= MapSerializer.class)
    private Map<String, Object> variables = new HashMap<String,Object>();
}
