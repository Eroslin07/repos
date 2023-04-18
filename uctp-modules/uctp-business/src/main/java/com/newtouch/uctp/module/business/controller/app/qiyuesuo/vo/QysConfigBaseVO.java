package com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 契约锁 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class QysConfigBaseVO {
    @Schema(description = "类型")
    private String code;

    @Schema(description = "访问地址")
    private String serverUrl;

    @Schema(description = "访问秘钥")
    private String accessKey;

    @Schema(description = "访问密匙")
    private String accessSecret;

    @Schema(description = "saas密匙")
    private String secret;

    @Schema(description = "状态(0未启用 1启用)")
    private Integer status;

    @Schema(description = "商户名称")
    private String businessName;

    @Schema(description = "商户Id")
    private Long businessId;

}
