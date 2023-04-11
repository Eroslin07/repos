package com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author helong
 * @date 2023/4/11 14:04
 */
@Schema(description = "管理后台 - 流程创建前基本信息获取 Request VO")
@Data
public class BpmCreateBaseInfoReqVO {

    @Schema(description = "商户名称", example = "东风二手车")
    //@NotEmpty(message = "商户名称不能为空")
    private String merchantName;
    @Schema(description = "流程业务标识（用于生成流程审批单号）", example = "merchantRegister")
    @NotEmpty(message = "流程业务标识不能为空")
    private String busiType;
}
