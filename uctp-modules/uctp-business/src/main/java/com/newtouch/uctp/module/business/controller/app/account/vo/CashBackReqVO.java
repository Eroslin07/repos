package com.newtouch.uctp.module.business.controller.app.account.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "资金管理 - 待回填保证金查询")
@Data
public class CashBackReqVO extends PageParam {
    @Schema(description = "商户账户号")
    @NotNull(message = "商户账户号不能为空")
    private String accountNo;
}
