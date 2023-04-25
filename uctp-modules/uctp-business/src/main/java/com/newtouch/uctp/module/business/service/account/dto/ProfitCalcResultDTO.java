package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class ProfitCalcResultDTO {
    // 现待回填保证金
    private Long nowWaitForBackCashTotalAmount;
    // 现利润余额
    private Long nowProfitTotalAmount;
    // 本次回填保证金（不含本次利润扣补回填保证金）
    private Long currentBackCashAmount;
    // 本次卖车款
    private Long currentCarSalesAmount;
    // 本次待回填保证金
    private Long currentWaitForBackCashAmount;
    // 使用本次利润抵扣原待回填保证金
    private Long useCurrentDeductionOriginalBackCashAmount;
    // 使用原有利润抵扣原待回填保证金
    private Long useOriginalDeductionOriginalBackCashAmount;
    // 使用原有利润抵扣本次回填保证金
    private Long useOriginalDeductionBackCashAmount;
    // 本次利润
    private Long currentProfitAmount;
    // 本次利润余额
    private Long currentProfitBalanceAmount;
    // 本次收车款
    private Long currentVehicleReceiptAmount;
    // 本次收益
    private Long currentRevenueAmount;
    // 本次总费用
    private Long currentFeeTotalAmount;
    // 本次费用明细
    private List<CostDTO> currentCosts;
    // 本次税收明细
    private List<TaxDTO> currentTaxes;
    // 计算时间
    private LocalDateTime calcTime;
}
