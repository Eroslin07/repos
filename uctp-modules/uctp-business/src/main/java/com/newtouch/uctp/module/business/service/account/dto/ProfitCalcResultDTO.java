package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ProfitCalcResultDTO {
    // 现待回填保证金
    private Integer waitForBackCashTotalAmount;
    // 现利润余额
    private Integer profitTotalAmount;
    // 本次待回填保证金
    private Integer waitForBackCashAmount;
    // 本次回填保证金（包含本次回填+本次扣补回填保证金）
    private Integer backCashAmount;
    // 本次扣补回填保证金
    private Integer deductionBackCashAmount;
    // 本次卖车利润
    private Integer profitAmount;
    // 本次费用明细
    private List<CostDTO> costs;
    // 本次税收明细
    private List<TaxDTO> taxes;
}
