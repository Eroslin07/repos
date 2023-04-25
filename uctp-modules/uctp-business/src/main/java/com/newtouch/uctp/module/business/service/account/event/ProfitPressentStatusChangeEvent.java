package com.newtouch.uctp.module.business.service.account.event;

import com.newtouch.uctp.module.business.enums.AccountEnum;
import lombok.ToString;

/**
 * 利润提现状态变更事件
 */
@ToString
public enum ProfitPressentStatusChangeEvent {
    CASH_BACK_DONE(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT, AccountEnum.PRESENT_PROFIT_CASH_BACK_DONE, "保证金已回填", null),
    APPLY(null, AccountEnum.PRESENT_PROFIT_APPLY, "申请登记", null),
    MARKET_AUDIT_PROCESSING(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, "市场方审核中", null),
    MARKET_AUDIT_REJECT(AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, AccountEnum.PRESENT_PROFIT_AUDIT_REJECT, "市场方审核退回", null),
    BANK_PROCESSING(AccountEnum.PRESENT_PROFIT_AUDIT_APPROVED, AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, "银行处理中", null),
    MARKET_AUDIT_APPROVED(AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING, AccountEnum.PRESENT_PROFIT_AUDIT_APPROVED, "市场方审核通过", BANK_PROCESSING),
    BANK_SUCCESS(AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, AccountEnum.PRESENT_PROFIT_BANK_SUCCESS, "银行处理成功", null),
    COST_PAY(AccountEnum.PRESENT_PROFIT_APPLY, AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, "费用支付", null),
    BANK_FAIL(AccountEnum.PRESENT_PROFIT_BANK_PROCESSING, AccountEnum.PRESENT_PROFIT_BANK_FAIL, "银行处理失败", null);

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
