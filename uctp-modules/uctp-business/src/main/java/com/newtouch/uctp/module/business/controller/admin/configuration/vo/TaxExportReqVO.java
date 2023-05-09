package com.newtouch.uctp.module.business.controller.admin.configuration.vo;

import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName TaxExportReqVO
 * @Author: zhang
 * @Date 2023/5/6
 */
@Schema(description = "配置管理 - 税率配置")
@Data
public class TaxExportReqVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "税费类型")
    @DictFormat(DictTypeConstants.TAX_TYPE)
    private String taxType;

    @Schema(description = "税率（%）")
    private String taxRate;

    @Schema(description = "税率生效日期始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectiveData;

    @Schema(description = "税率生效日期止")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expirationData;

    @Schema(description = "操作人")
    private String createdBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;
}
