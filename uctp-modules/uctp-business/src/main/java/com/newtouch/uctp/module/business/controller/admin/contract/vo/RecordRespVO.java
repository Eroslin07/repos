package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName RecordRespVO
 * @Author: zhang
 * @Date 2023/4/21.
 */
@Schema(description = "合同管理 - 合同档案分页项 Response VO")
@Data
public class RecordRespVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "合同编号")
    private String number;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "甲方")
    private String first;

    @Schema(description = "乙方")
    private String second;

    @Schema(description = "商户")
    private String merchant;

    @Schema(description = "合同类型")
    private Integer type;

    @Schema(description = "签约金额")
    private String amount;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "发起时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime initiationTime;

    @Schema(description = "签约时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime signTime;

    @Schema(description = "发起人")
    private String initiator;

    @Schema(description = "更新人")
    private String updater;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "租户号")
    private String tenantId;
}
