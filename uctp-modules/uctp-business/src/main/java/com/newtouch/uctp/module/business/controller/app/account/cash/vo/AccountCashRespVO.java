package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Schema(description = "账户保证金详情")
@Data
public class AccountCashRespVO {

    @Schema(description = "账户号")
    private String accountNo;

    @Schema(description = "保证金")
    private Integer cash;

    @Schema(description = "保证金-冻结余额")
    private Integer freezeCash;

    @Schema(description = "保证金-可用余额")
    private Integer availableCash;

    @Schema(description = "保证金交易明细")
    private List<CashDetailRespVO> cashDetails;

    public static AccountCashRespVO build(MerchantAccountDO merchantAccountDO, List<MerchantCashDO> merchantCashList) {
        AccountCashRespVO accountCashRespVO = new AccountCashRespVO();

        BeanUtils.copyProperties(merchantAccountDO, accountCashRespVO);

        accountCashRespVO.setCashDetails(
                CollectionUtil.isNotEmpty(merchantCashList) ?
                        merchantCashList.stream().map(CashDetailRespVO::build).collect(Collectors.toList()) : Collections.emptyList());
        return accountCashRespVO;
    }
}
