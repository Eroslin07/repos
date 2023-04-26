package com.newtouch.uctp.module.business.api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "商户开立虚拟子账户号")
@Data
public class AccountDTO {

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "商户ID")
    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    @Schema(description = "商户公司名称")
    @NotNull(message = "商户名称不能为空")
    private String merchantCompanyName;

    @Schema(description = "商户用户姓名")
    @NotNull(message = "商户用户姓名不能为空")
    private String merchantName;

    @Schema(description = "开户行名称")
    @NotNull(message = "开户行名称不能为空")
    private String bankName;

    @Schema(description = "对公银行账号")
    @NotNull(message = "对公银行账号不能为空")
    private String bankNo;

    @Schema(description = "保证金充值银行账号")
    @NotNull(message = "保证金充值银行账号不能为空")
    private String cashBankNo;
}
