package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Schema(description = "商户银行信息")
@Data
public class MerchantBankRespVO {

    @Schema(description = "账户号")
    private String accountNo;

    @Schema(description = "保证金交易银行账号")
    private String bankNo;

    @Schema(description = "保证金交易开户银行名称")
    private String bankName;
}
