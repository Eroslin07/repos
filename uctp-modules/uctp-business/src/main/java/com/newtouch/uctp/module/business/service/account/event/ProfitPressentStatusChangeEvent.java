package com.newtouch.uctp.module.business.service.account.event;

import com.newtouch.uctp.module.business.enums.AccountEnum;
import lombok.ToString;

/**
 * 利润提现状态变更事件
 */
@ToString
public enum ProfitPressentStatusChangeEvent {
    // 回填保证金入账事件
    CASH_BACK_SUCCESS(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT, AccountEnum.PRESENT_PROFIT_CASH_BACK_DONE, "回填保证金入账成功", null),

    // 利润提现事件
    PRESENT_APPLY(null, AccountEnum.PRESENT_PROFIT_APPLY, "利润提现申请", null),
    PRESENT_MARKET_AUDIT_PROCESSING(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, "利润提现申场方审核中", null),
    PRESENT_MARKET_AUDIT_REJECT(AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, AccountEnum.PRESENT_PROFIT_AUDIT_REJECT, "利润提现市场方审核退回", null),
    PRESENT_BANK_PROCESSING(AccountEnum.PRESENT_PROFIT_AUDIT_APPROVED, AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, "利润提现银行处理中", null),
    PRESENT_MARKET_AUDIT_APPROVED(AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, AccountEnum.PRESENT_PROFIT_AUDIT_APPROVED, "利润提现市场方审核通过", PRESENT_BANK_PROCESSING),
    PRESENT_BANK_SUCCESS(AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "利润提现银行处理成功", null),
    PRESENT_BANK_FAIL(AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, AccountEnum.PRESENT_PROFIT_BANK_FAIL, "利润提现银行处理失败", null),

    // 费用入账事件
    COST_TRANSFER_SUCCESS(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "服务费入账成功", null),
    COST_TRANSFER_FAIL(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_BANK_FAIL, "服务费入账失败", null),
    TAX_TRANSFER_SUCCESS(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "税费入账成功", null),
    TAX_TRANSFER_FAIL(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_BANK_FAIL, "税费入账失败", null),

    // 利润入账事件
    PROFIT_TRANSFER_SUCCESS(null, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "利润入账成功", null),
    PROFIT_TRANSFER_FAIL(null, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "利润入账失败", null);

    private AccountEnum sourceStatus; // 源状态
    private AccountEnum targetStatus; // 目标状态
    private String description; // 事件描述
    private ProfitPressentStatusChangeEvent postEvent; // 后置事件

    ProfitPressentStatusChangeEvent(AccountEnum sourceStatus, AccountEnum targetStatus, String description, ProfitPressentStatusChangeEvent postEvent) {
        this.sourceStatus = sourceStatus;
        this.targetStatus = targetStatus;
        this.description = description;
        this.postEvent = postEvent;
    }

    public AccountEnum getSourceStatus() {
        return sourceStatus;
    }

    public AccountEnum getTargetStatus() {
        return targetStatus;
    }

    public String getDescription() {
        return description;
    }

    public ProfitPressentStatusChangeEvent getPostEvent() {
        return postEvent;
    }
}
