package com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

/**
 * @author helong
 * @date 2023/4/13 13:24
 */
@Schema(description = "管理后台 - 流程实例的创建 Request VO")
@Data
public class BpmProcessInstanceByKeyReqVO {
    @Schema(description = "流程定义标识（业务类型）", required = true, example = "WGSC")
    @NotEmpty(message = "流程定义标识（业务类型）不能为空")
    private String procDefKey;

    @Schema(description = "变量实例")
    private Map<String, Object> variables = new HashMap<String, Object>();
}
