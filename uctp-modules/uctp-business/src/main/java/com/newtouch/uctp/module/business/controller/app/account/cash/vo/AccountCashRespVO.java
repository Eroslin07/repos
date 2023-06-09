package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Schema(description = "账户保证金详情")
@Data
public class AccountCashRespVO {

    @Schema(description = "账户号")
    private String accountNo;

    @Schema(description = "保证金（单位：分）")
    private Long cash;

    @Schema(description = "保证金-冻结余额（单位：分）")
    private Long freezeCash;

    @Schema(description = "保证金-可用余额（单位：分）")
    private Long availableCash;

    @Schema(description = "版本号")
    private Integer revision;

    @Schema(description = "保证金提现中冻结金额（单位：分）")
    private Long withdrawFreezeCash;

    @Schema(description = "保证金交易银行账号")
    private String bankNo;

    @Schema(description = "保证金交易开户银行名称")
    private String bankName;

    @Schema(description = "保证金交易明细")
    private List<CashDetailRespVO> cashDetails;

    public static AccountCashRespVO build(MerchantAccountDO merchantAccountDO, List<CashDetailRespVO> merchantCashList) {
        AccountCashRespVO accountCashRespVO = new AccountCashRespVO();
        BeanUtils.copyProperties(merchantAccountDO, accountCashRespVO);
        accountCashRespVO.setCashDetails(merchantCashList);
        return accountCashRespVO;
    }
}
