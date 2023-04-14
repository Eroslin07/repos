package com.newtouch.uctp.module.business.service.account.impl;

import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxRateDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class AccountProfitServiceImpl implements AccountProfitService {
    @Override
    public void generateProfit(String accountNo,
                               String contractNo,
                               BigDecimal vehicleReceiptAmount,
                               BigDecimal carSalesAmount,
                               List<CostDTO> costs,
                               List<TaxRateDTO> taxRates) {
        // 参数校验
        this.generateProfitCheck(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount, costs);
        // 获得总费用
        BigDecimal totalCostAmount = this.getTotalCostAmount(costs);
        // 计算利润，利润=卖车金额-所有费用-收车金额
        BigDecimal profit = carSalesAmount.divide(totalCostAmount).divide(carSalesAmount);
    }

    private void generateProfitCheck(String accountNo,
                                     String contractNo,
                                     BigDecimal vehicleReceiptAmount,
                                     BigDecimal carSalesAmount,
                                     List<CostDTO> costs) {
        if (StringUtils.isBlank(accountNo)) {
            throw exception(ACC_ACCOUNT_NO_NOT_NULL);
        }
        if (StringUtils.isBlank(contractNo)) {
            throw exception(ACC_CONTRACT_NO_NOT_NULL);
        }
        if (vehicleReceiptAmount == null) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL);
        }
        if (vehicleReceiptAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
        if (vehicleReceiptAmount == null) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL);
        }
        if (vehicleReceiptAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
    }

    private BigDecimal getTotalCostAmount(List<CostDTO> costs) {
        BigDecimal r = BigDecimal.ZERO;
        if (costs != null && !costs.isEmpty()) {
            Set<String> costTypeSet = new HashSet<>();
            for (CostDTO cost : costs) {
                String costType = cost.getType();
                BigDecimal costAmount = cost.getAmount();

                if (costTypeSet.contains(costType)) {
                    throw exception(ACC_COST_TYPE_REPEAT);
                }

                costTypeSet.add(costType);
                if (costAmount != null) {
                    // 费用金额不为空时添加进总费用
                    r = r.add(costAmount);
                }
            }
        }

        return r;
    }
}
