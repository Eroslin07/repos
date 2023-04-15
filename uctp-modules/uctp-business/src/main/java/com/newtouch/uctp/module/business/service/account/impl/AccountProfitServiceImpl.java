package com.newtouch.uctp.module.business.service.account.impl;

import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.ProfitCalcResultDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxDTO;
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
    public List<MerchantProfitDO> recorded(String accountNo,
                                           String contractNo,
                                           Integer vehicleReceiptAmount,
                                           Integer carSalesAmount,
                                           List<CostDTO> costs,
                                           List<TaxDTO> taxes) {
        // 参数校验
        this.recordedCheck(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount, costs);
        // 计算本次利润
        ProfitCalcResultDTO profitCalcResult = this.calcProfit(accountNo, vehicleReceiptAmount, carSalesAmount, costs, taxes);

        // todo
        return null;
    }

    /**
     * 计算利润
     * @param accountNo
     * @param vehicleReceiptAmount
     * @param carSalesAmount
     * @param costs
     * @param taxes
     * @return
     */
    private ProfitCalcResultDTO calcProfit(String accountNo,
                                          Integer vehicleReceiptAmount,
                                          Integer carSalesAmount,
                                          List<CostDTO> costs,
                                          List<TaxDTO> taxes) {
        // 原待回填保证金=账户表的待回填保证金
        Integer originalWaitForBackCashTotalAmount = this.getOriginalWaitForBackCashTotalAmount(accountNo);

        // 原利润余额=账户表的利润
        Integer originalProfitTotalAmount = this.getOriginalProfitTotalAmount(accountNo);

        // 服务费总额=各项费用之和
        Integer costTotalAmount = this.calcCostAmount(costs);

        // 税费总额=各项税费之和
        Integer taxTotalAmount = this.calcTaxAmount(carSalesAmount, taxes);

        // 总费用=服务费总额+税费总额
        Integer tmpFeeTotalAmount = costTotalAmount + taxTotalAmount;

        // 本次收益=卖车价-收车价-总费用
        Integer tmpTheRevenueAmount = carSalesAmount - vehicleReceiptAmount - tmpFeeTotalAmount;

        // 本次可用回填保证金=原利润余额+卖车价-总费用
        Integer tmpTheAvailableBackCashAmount = originalProfitTotalAmount + carSalesAmount - tmpFeeTotalAmount;

        // 本次应回填保证金合计=原待回填保证金+收车价
        Integer tmpTheShouldBackCashTotalAmount = originalWaitForBackCashTotalAmount + vehicleReceiptAmount;

        // 本次实回填保证金=IF(本次应回填保证金合计<=本次可用回填保证金,本次应回填保证金合计,本次可用回填保证金)
        Integer tmpTheActualBackCashAmount = tmpTheShouldBackCashTotalAmount.compareTo(tmpTheAvailableBackCashAmount) <= 0 ?
                tmpTheShouldBackCashTotalAmount : tmpTheAvailableBackCashAmount;

        // 本次应回填保证金=收车价
        Integer tmpTheShouldBackCashAmount = vehicleReceiptAmount;

        // 本次待回填保证金=IF(本次收益>0,0,本次应回填保证金-本次实回填保证金)
        Integer theWaitForBackCashAmount = tmpTheRevenueAmount.compareTo(0) > 0 ? 0 : tmpTheShouldBackCashAmount - tmpTheActualBackCashAmount;

        // 本次回填保证金=本次实回填保证金
        Integer theBackCashAmount = tmpTheActualBackCashAmount;

        // 本次卖车利润=卖车价
        Integer theProfitAmount = carSalesAmount;

        // 本次扣补回填保证金=IF(本次实回填保证金>本次应回填保证金,本次实回填保证金-本次应回填保证金,0)
        Integer theDeductionBackCashAmount = tmpTheActualBackCashAmount.compareTo(tmpTheShouldBackCashTotalAmount) >0 ?
                tmpTheActualBackCashAmount - tmpTheShouldBackCashTotalAmount : 0;

        // 现待回填保证金=本次待回填保证金+原待回填保证金-本次扣补回填保证金
        Integer nowWaitForBackCashTotalAmount = theWaitForBackCashAmount + originalWaitForBackCashTotalAmount - theDeductionBackCashAmount;

        // 现利润余额=原利润余额+本次收益-(本次实回填保证金-收车价)
        Integer nowProfitTotalAmount = originalProfitTotalAmount + tmpTheRevenueAmount - tmpTheActualBackCashAmount - vehicleReceiptAmount;

        ProfitCalcResultDTO result = ProfitCalcResultDTO.builder()
                // 本次回填保证金
                .backCashAmount(theBackCashAmount)
                // 本次卖车利润
                .profitAmount(theProfitAmount)
                // 本次扣减补保证金
                .deductionBackCashAmount(theDeductionBackCashAmount)
                // 本次待回填保证金
                .waitForBackCashAmount(theWaitForBackCashAmount)
                // 现利润总额
                .profitTotalAmount(nowProfitTotalAmount)
                // 现待回填保证金总额
                .waitForBackCashTotalAmount(nowWaitForBackCashTotalAmount)
                // 本次税收
                .taxes(taxes)
                // 本次费用
                .costs(costs)
                .build();

        return result;
    }

    /**
     * 计算税费，并返回税费总和
     * @param carSalesAmount 卖车价
     * @param taxes 税
     * @return 剩总和
     */
    private Integer calcTaxAmount(Integer carSalesAmount, List<TaxDTO> taxes) {
        Integer taxTotalAmouont = 0;
        if (taxes != null && !taxes.isEmpty()) {
            Set<String> taxTypeSet = new HashSet<>();
            for (TaxDTO tax : taxes) {
                String taxType = tax.getType();
                if (taxTypeSet.contains(taxType)) {
                    throw exception(ACC_TAX_TYPE_REPEAT);
                }
                BigDecimal rate = tax.getRate();
                if (rate != null) {
                    // 金额为分，计算后取整数
                    Integer taxAmount = rate.multiply(BigDecimal.valueOf(carSalesAmount)).intValue();
                    // 回写税收对象
                    tax.setAmount(taxAmount);
                    taxTotalAmouont = taxTotalAmouont + taxAmount;
                } else {
                    tax.setAmount(0); // 税率为空时，税费为0
                }
            }
        }
        return taxTotalAmouont;
    }

    /**
     * 计算所有费用
     * @param costs 费用
     * @return 费用总和
     */
    private Integer calcCostAmount(List<CostDTO> costs) {
        Integer r = 0;
        if (costs != null && !costs.isEmpty()) {
            Set<String> costTypeSet = new HashSet<>();
            for (CostDTO cost : costs) {
                String costType = cost.getType();
                Integer costAmount = cost.getAmount();

                if (costTypeSet.contains(costType)) {
                    throw exception(ACC_COST_TYPE_REPEAT);
                }

                costTypeSet.add(costType);
                if (costAmount != null) {
                    // 费用金额不为空时添加进总费用
                    r = r + costAmount;
                }
            }
        }

        return r;
    }

    /**
     * 获取原有利润总额
     * @param accountNo
     * @return
     */
    private Integer getOriginalProfitTotalAmount(String accountNo) {
        // todo
        return 0;
    }

    /**
     * 获取原有待回填保证金总额
     * @param accountNo
     * @return
     */
    private Integer getOriginalWaitForBackCashTotalAmount(String accountNo) {
        // todo
        return 0;
    }

    /**
     * 利润划入参数校验
     * @param accountNo
     * @param contractNo
     * @param vehicleReceiptAmount
     * @param carSalesAmount
     * @param costs
     */
    private void recordedCheck(String accountNo,
                                     String contractNo,
                                     Integer vehicleReceiptAmount,
                                     Integer carSalesAmount,
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
        if (vehicleReceiptAmount.compareTo(0) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
        if (vehicleReceiptAmount == null) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL);
        }
        if (vehicleReceiptAmount.compareTo(0) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
    }

}
