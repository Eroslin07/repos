package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCashReqVO extends PageParam {

    @Schema(description = "账户号")
    @NotEmpty(message = "商户账户号不能为空")
    private String accountNo;

}
