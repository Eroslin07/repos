package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxRateDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 利润服务接口
 *
 * @author zhangjun
 */
public interface AccountProfitService {


    /**
     * 计算利润，并调用保证金服务回填保证金
     * @param accountNo 商户号
     * @param contractNo 交易合同号
     * @param vehicleReceiptAmount 收车金额
     * @param carSalesAmount 卖车金额
     * @param costs 费用清单
     * @param taxRates 税率清单
     */
    void generateProfit(String accountNo,
                        String contractNo,
                        BigDecimal vehicleReceiptAmount,
                        BigDecimal carSalesAmount,
                        List<CostDTO> costs,
                        List<TaxRateDTO> taxRates);
}
