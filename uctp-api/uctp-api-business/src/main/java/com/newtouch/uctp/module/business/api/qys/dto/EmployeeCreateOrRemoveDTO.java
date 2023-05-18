package com.newtouch.uctp.module.business.api.qys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeCreateOrRemoveDTO {
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @NotNull
    private String mobile;
    /**
     * 名字
     */
    @Schema(description = "名字")
    @NotNull
    private String nickname;
    /**
     * 部门id
     */
    @Schema(description = "部门id")
    @NotNull
    private Long deptId;
    /**
     * 是否停用
     */
    @Schema(description = "是否停用")
    @NotNull
    private Integer status;
    /**
     * 是否添加权限
     */
    @Schema(description = "是否添加权限")
    private Boolean isRole;

    public EmployeeCreateOrRemoveDTO() {
        this.mobile = mobile;
        this.nickname = nickname;
        this.deptId = deptId;
        this.status = status;
        this.isRole = isRole;
    }
}
