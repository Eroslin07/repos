package com.newtouch.uctp.module.business.controller.app.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(description = "POS机管理 - POS机命名")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosBindReqVO {

    /**
     * 商户号
     */
    @Schema(description = "商户号")
    @NotNull(message = "商户号不能为空")
    private Long merchantId;

    /**
     * POS机名称
     */
    @Schema(description = "POS机商户编号")
    @NotNull(message = "POS机商户编号不能为空")
    private String posMrhNo;
}
