package com.newtouch.uctp.module.business.controller.admin.configuration.vo;
import com.newtouch.uctp.framework.common.pojo.PageParam;
import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName TempReqVO
 * @Author: zhang
 * @Date 2023/5/6
 */
@Schema(description = "配置管理 - 税率配置分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaxReqVO extends PageParam {
    @Schema(description = "税费类型")
    @DictFormat(DictTypeConstants.TAX_TYPE)
    private String taxType;

    @Schema(description = "操作人")
    private String createdBy;



}
