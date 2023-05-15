package com.newtouch.uctp.module.business.controller.admin.configuration.vo;
import com.newtouch.uctp.framework.common.pojo.PageParam;
import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName CostReqVO
 * @Author: zhang
 * @Date 2023/5/13
 */
@Schema(description = "配置管理 - 税率配置分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CostReqVO extends PageParam {
    @Schema(description = "费用类型")
    @DictFormat(DictTypeConstants.TAX_TYPE)
    private Integer costType;

    @Schema(description = "操作人")
    private String createdBy;



}
