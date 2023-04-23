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
    private Integer nowWaitForBackCashTotalAmount;
    // 现利润余额
    private Integer nowProfitTotalAmount;
    // 本次回填保证金（不含本次利润扣补回填保证金）
    private Integer currentBackCashAmount;
    // 本次卖车款
    private Integer currentCarSalesAmount;
    // 本次待回填保证金
    private Integer currentWaitForBackCashAmount;
    // 本次使用本次利润抵扣金额
    private Integer useCurrentDeductionBackCashAmount;
    // 本次使用原有利润抵扣金额
    private Integer useOriginalDeductionBackCashAmount;
    // 本次利润（纯利）
    private Integer currentProfitAmount;
    // 本次收益
    private Integer currentRevenueAmount;
    // 本次总费用
    private Integer currentFeeTotalAmount;
    // 本次费用明细
    private List<CostDTO> currentCosts;
    // 本次税收明细
    private List<TaxDTO> currentTaxes;
    // 计算时间
    private LocalDateTime calcTime;
}
