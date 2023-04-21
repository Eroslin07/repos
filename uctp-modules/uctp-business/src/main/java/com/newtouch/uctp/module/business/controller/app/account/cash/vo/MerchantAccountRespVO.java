package com.newtouch.uctp.module.business.controller.app.account.cash.vo;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "商户虚拟账户资产详情")
@Data
public class MerchantAccountRespVO {

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "账户号")
    private String accountNo;

    @Schema(description = "保证金")
    private Integer cash;

    @Schema(description = "利润")
    private Integer profit;

    @Schema(description = "商户标识")
    private Long merchantId;

    public static MerchantAccountRespVO build(MerchantAccountDO accountDO) {
        MerchantAccountRespVO respVO = new MerchantAccountRespVO();

        if (null != accountDO) {
            respVO.setMobile(accountDO.getMobile());
            respVO.setAccountNo(accountDO.getAccountNo());
            respVO.setCash(accountDO.getCash());
            int profit = 0;
            if (accountDO.getProfit() != null) {
                profit += accountDO.getProfit();
            }

            if (accountDO.getFreezeProfit() != null) {
                profit += accountDO.getFreezeProfit();
            }
            respVO.setProfit(profit);
            respVO.setMerchantId(accountDO.getMerchantId());
        }

        return respVO;
    }
}
