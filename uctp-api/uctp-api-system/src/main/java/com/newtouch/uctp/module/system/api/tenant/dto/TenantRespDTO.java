package com.newtouch.uctp.module.system.api.tenant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

/**
 * @author helong
 * @date 2023/4/11 14:38
 */
@Data
public class TenantRespDTO {
    @Schema(description = "租户编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

    @Schema(description = "租户名", required = true, example = "芋道")
    @NotNull(message = "租户名不能为空")
    private String name;

    @Schema(description = "联系人", required = true, example = "芋艿")
    @NotNull(message = "联系人不能为空")
    private String contactName;

    @Schema(description = "联系手机", example = "15601691300")
    private String contactMobile;

    @Schema(description = "租户状态", required = true, example = "1")
    @NotNull(message = "租户状态")
    private Integer status;

    @Schema(description = "绑定域名", example = "https://www.iocoder.cn")
    @URL(message = "绑定域名的地址非 URL 格式")
    private String domain;

    @Schema(description = "租户属性，参见 TenantPackageTypeEnum 枚举", required = true, example = "1" )
    @NotNull(message = "租户属性不能为空")
    private Integer type;

    @Schema(description = "租户套餐编号", required = true, example = "1024")
    @NotNull(message = "租户套餐编号不能为空")
    private Long packageId;

    @Schema(description = "过期时间", required = true)
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expireTime;

    @Schema(description = "账号数量", required = true, example = "1024")
    @NotNull(message = "账号数量不能为空")
    private Integer accountCount;
}
