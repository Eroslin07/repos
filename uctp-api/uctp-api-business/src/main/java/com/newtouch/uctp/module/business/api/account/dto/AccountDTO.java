package com.newtouch.uctp.module.business.api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "商户开立虚拟子账户号")
@Data
public class AccountDTO {

    @Schema(description = "身份证号", required = true, example = "513021")
    private String idCard;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "商户ID")
    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    @Schema(description = "公司名称", required = true, example = "XX车行")
    private String businessName;

    @Schema(description = "法定代表人")
    private String legalRepresentative;

    @Schema(description = "营业执照号", required = true, example = "123456")
    private String taxNum;

    @Schema(description = "开户行名称")
    @NotNull(message = "开户行名称不能为空")
    private String bankName;

    @Schema(description = "对公银行账号")
    @NotNull(message = "对公银行账号不能为空")
    private String bankNo;

    @Schema(description = "保证金充值银行账号")
    @NotNull(message = "保证金充值银行账号不能为空")
    private String cashBankNo;

    @Schema(description = "商户交易方式")
    @NotNull(message = "商户交易方式不能为空!")
    private Integer tranWay;
}
