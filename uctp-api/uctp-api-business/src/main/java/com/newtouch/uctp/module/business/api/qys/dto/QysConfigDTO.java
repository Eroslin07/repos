package com.newtouch.uctp.module.business.api.qys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QysConfigDTO {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "访问地址")
    private String serverUrl;
    @Schema(description = "访问秘钥")
    private String accessKey;
    @Schema(description = "访问密匙")
    private String accessSecret;
    @Schema(description = "saas秘钥")
    private String secret;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "状态(0启用 1未启用)")
    private Integer status;
    @Schema(description = "商户名称")
    private String businessName;
    @Schema(description = "商户Id")
    private Long businessId;
    @Schema(description = "契约锁公司id")
    private Long companyId;
    @Schema(description = "契约锁公司公章id")
    private Long sealId;
}
