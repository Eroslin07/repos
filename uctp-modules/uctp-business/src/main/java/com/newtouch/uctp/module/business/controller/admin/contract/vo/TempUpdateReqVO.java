package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName TempUpdateReqVO
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Schema(description = "合同管理 - 合同模板 Request VO")
@Data
public class TempUpdateReqVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "模板编号")
    private String number;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "合同类型")
    private Integer type;

    @Schema(description = "说明")
    private String remark;

    @Schema(description = "状态")
    private Integer status;
}
