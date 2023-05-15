package com.newtouch.uctp.module.business.controller.admin.configuration.vo;

import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName CostExportReqVO
 * @Author: zhang
 * @Date 2023/5/13
 */
@Schema(description = "配置管理 - 税率配置")
@Data
public class CostExportReqVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "费用类型")
    @DictFormat(DictTypeConstants.TAX_TYPE)
    private Integer costType;

    @Schema(description = "费用（元）")
    private Integer cost;

    @Schema(description = "车辆类型")
    private String vehicleType;

    @Schema(description = "费用生效日期始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectiveData;

    @Schema(description = "费用生效日期止")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expirationData;

    @Schema(description = "操作人")
    private String createdBy;

    @Schema(description = "创建时间", required = true, example = "时间戳格式")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间", required = true, example = "时间戳格式")
    private LocalDateTime updateTime;
}
