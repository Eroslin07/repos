package com.newtouch.uctp.module.bpm.controller.admin.workbench.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import com.newtouch.uctp.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务的 TODO 待办的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TodoListPageReqVO extends PageParam {

    @Schema(description = "申请单号")
    private String id;

    @Schema(description = "事项")
    private String name;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "申请时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;

}
