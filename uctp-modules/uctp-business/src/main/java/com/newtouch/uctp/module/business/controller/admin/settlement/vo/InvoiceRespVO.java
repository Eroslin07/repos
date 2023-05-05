package com.newtouch.uctp.module.business.controller.admin.settlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author zhang
 * @date 2023/4/17 13:46
 */
@Schema(description = "结束中心 - 发票管理分页项 Response VO")
@Data
public class InvoiceRespVO {

    @Schema(description = "序号")
    private Long id;

    @Schema(description = "发票类型")
    private Integer type;

    @Schema(description = "类型")
    private String typeName;

    @Schema(description = "商户")
    private String merchant;

    @Schema(description = "合同编号")
    private String number;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "合同金额")
    private String amount;

    @Schema(description = "税额")
    private String tax;

    @Schema(description = "发票状态")
    private Integer status;

    @Schema(description = "状态")
    private String statusName;

    @Schema(description = "租户号")
    private String tenantId;

    @Schema(description = "创建人")
    private String founder;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime creationTime;

    @Schema(description = "更新人")
    private String updater;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "乐观锁")
    private Long optimistic;
}
