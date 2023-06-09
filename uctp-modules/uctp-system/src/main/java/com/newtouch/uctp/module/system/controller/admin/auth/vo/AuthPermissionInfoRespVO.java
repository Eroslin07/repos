package com.newtouch.uctp.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Schema(description = "管理后台 - 登录用户的权限信息 Response VO，额外包括用户信息和角色列表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthPermissionInfoRespVO {

    @Schema(description = "用户信息", required = true)
    private UserVO user;

    @Schema(description = "角色标识数组", required = true)
    private Set<String> roles;

    @Schema(description = "操作权限数组", required = true)
    private Set<String> permissions;

    @Schema(description = "用户信息 VO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserVO {

        @Schema(description = "用户编号", required = true, example = "1024")
        private Long id;

        @Schema(description = "用户昵称", required = true, example = "芋道源码")
        private String nickname;

        @Schema(description = "用户头像", required = true, example = "http://www.iocoder.cn/xx.jpg")
        private String avatar;

        @Schema(description = "租户id")
        private Long tenantId;

        @Schema(description = "商户id")
        private Long deptId;

        @Schema(description = "商户名称")
        private String deptName;

        @Schema(description = "市场名称")
        private String tenantName;

        @Schema(description = "用户类型(1 主账号 2 商户子账号)")
        private String staffType;

        @Schema(description = "认证状态（0已认证 1未认证）")
        private Integer registerType;

        @Schema(description = "虚拟账号")
        private String accountNo;

        @Schema(description = "交易方式")
        private Integer paymentType;

    }

}
