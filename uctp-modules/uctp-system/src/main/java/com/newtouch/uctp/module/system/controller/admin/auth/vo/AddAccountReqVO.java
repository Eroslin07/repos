package com.newtouch.uctp.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = " 新增子账号 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAccountReqVO {
    @Schema(description = "账号id（修改时传递）")
    private Long id;

    @Schema(description = "姓名", required = true, example = "张三")
    private String name;

    @Schema(description = "手机号", required = true, example = "13888888888")
    private String phone;

    @Schema(description = "身份证号", required = true, example = "513021")
    private String idCard;

    @Schema(description = "是否停用(0正常 1停用)" )
    private String status;

    @Schema(description = "商户id")
    private Long deptId;

    @Schema(description = "租户id")
    private Long tenantId;
}
