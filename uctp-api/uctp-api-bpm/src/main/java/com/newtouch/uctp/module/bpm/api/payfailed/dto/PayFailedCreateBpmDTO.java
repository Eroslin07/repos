package com.newtouch.uctp.module.bpm.api.payfailed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * @author helong
 * @date 2023/4/28 18:35
 */
@Schema(description = "管理后台 - 支付失败流程实例的创建 Request VO")
@Data
public class PayFailedCreateBpmDTO {
    @Schema(description = "支付侧合同号ID（对应契约锁的合同ID）")
    @NotNull(message = "支付侧合同号ID（对应契约锁的合同ID）不能为空")
    private Long contractId;
    @Schema(description = "变量实例")
    private Map<String, Object> variables = new HashMap<String, Object>();
}
