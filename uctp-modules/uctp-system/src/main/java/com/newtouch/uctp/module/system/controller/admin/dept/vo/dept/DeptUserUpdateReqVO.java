package com.newtouch.uctp.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 部门更新 Request VO")
@Data
public class DeptUserUpdateReqVO {
    @Schema(description = "部门id")
    private Long id;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", required = true, example = "1")
    private Integer status;
}