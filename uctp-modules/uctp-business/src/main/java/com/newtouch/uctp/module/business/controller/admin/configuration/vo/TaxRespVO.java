package com.newtouch.uctp.module.business.controller.admin.configuration.vo;
import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @ClassName TaxRespVO
 * @Author: zhang
 * @Date 2023/5/6
 */
@Schema(description = "配置管理 - 税率配置 Response VO")
@Data
public class TaxRespVO {
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

    @Schema(description = "商户ID")
    private Long businessId;

    @Schema(description = "创建时间", required = true, example = "时间戳格式")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间", required = true, example = "时间戳格式")
    private LocalDateTime updateTime;

    @Schema(description = "乐观锁")
    private Long optimistic;

    @Schema(description = "租户号")
    private Long tenantId;
}
