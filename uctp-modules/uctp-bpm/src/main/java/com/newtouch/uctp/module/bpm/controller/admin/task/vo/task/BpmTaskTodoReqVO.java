package com.newtouch.uctp.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.newtouch.uctp.framework.common.pojo.PageParam;

/**
 * @author helong
 * @date 2023/4/11 16:03
 */
@Schema(description = "管理后台 - 流程任务的待办分页项 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskTodoReqVO extends PageParam {
    @Schema(description = "申请人", example = "小王")
    private String startUserName;
    @Schema(description = "申请时间", example = "2023-01-03")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyDate;
    @Schema(description = "申请事项", example = "收车申请")
    private String title;
    @Schema(description = "商户", example = "小二二手车")
    private String merchantName;
    @Schema(description = "申请单号", example = "SQ202301010001")
    private String serialNo;
}
