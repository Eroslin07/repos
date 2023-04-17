package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.module.business.service.impl.ValidatedGroup.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "账户保证金充值提取 Request VO")
@Data
public class TransactionRecordReqVO {

    @Schema(description = "账户号")
    @NotEmpty(message = "商户账户号不能未空",
            groups = {Recharge.class, Withdraw.class, Reserve.class, Difference.class, Back.class, ProfitBack.class})
    private String accountNo;

    @Schema(description = "交易金额")
    @Min(value = 1, message = "交易金额必须大于0",
            groups = {Recharge.class, Withdraw.class, Reserve.class, Back.class, ProfitBack.class})
    private Integer tranAmount;

    @Schema(description = "版本号")
    @NotNull(message = "版本号不能为空",
            groups = {Recharge.class, Withdraw.class})
    private Integer revision;

    @Schema(description = "交易合同号")
    @NotEmpty(message = "交易合同号不能为空",
            groups = {Reserve.class, Deduction.class, Back.class, ProfitBack.class, Release.class})
    private String contractNo;

}
