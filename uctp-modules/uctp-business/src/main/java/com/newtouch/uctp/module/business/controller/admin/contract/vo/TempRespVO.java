package com.newtouch.uctp.module.business.controller.admin.contract.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName TepmRespVO
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Schema(description = "合同管理 - 合同模板分页项 Response VO")
@Data
public class TempRespVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "模板编号")
    private String number;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "合同类型")
    private Integer type;

    @Schema(description = "类型名")
    private String typeName;

    @Schema(description = "说明")
    private String remark;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "状态名")
    private String statusName;

    @Schema(description = "操作人")
    private String createdBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createdTime;

    @Schema(description = "更新人")
    private String updater;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "乐观锁")
    private Long optimistic;

    @Schema(description = "租户号")
    private String tenantId;
}
