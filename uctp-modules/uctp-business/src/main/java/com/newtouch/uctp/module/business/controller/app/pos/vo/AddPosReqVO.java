package com.newtouch.uctp.module.business.controller.app.pos.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = " 新增pos机 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPosReqVO {
    @Schema(description = "主表id（修改时传递）")
    private Long id;

    @Schema(description = "POS机名称", required = true, example = "XX")
    private String posName;

    @Schema(description = "POS机编号", required = true, example = "2321211")
    private String posId;

    @Schema(description = "备注", required = true, example = "xxx")
    private String remark;

    @Schema(description = "是否停用(0正常 1停用)" )
    private String status;

    @Schema(description = "商户id")
    private Long deptId;

    @Schema(description = "租户id")
    private Long tenantId;
}
