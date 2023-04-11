package com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author helong
 * @date 2023/4/11 14:15
 */
@Schema(description = "管理后台 - 流程创建前基本信息获取 Resp VO")
@Data
public class BpmCreateBaseInfoRespVO {
    @Schema(description = "流程业务标识（用于生成流程审批单号）", example = "merchantRegister")
    private String busiType;
    @Schema(description = "商户ID", example = "10001")
    private Long merchantId;
    @Schema(description = "申请单号", example = "busiType + 年月日 + 5位流水号")
    private String serialNo;
    @Schema(description = "标题（事项）", example = "山西万国市场商户张三账号申请")
    private String title;
    @Schema(description = "业务ID", example = "100001")
    Long businessKey;
    @Schema(description = "流程定义ID", example = "100001")
    private String procDefId;
}
