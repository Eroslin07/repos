package com.newtouch.uctp.module.business.service.account.event;

import lombok.ToString;

import static com.newtouch.uctp.module.business.enums.AccountConstants.*;

/**
 * 利润提现状态变更事件
 */
@ToString
public enum ProfitPressentStatusChangeEvent {
    CASH_BACK_DONE(PRESENT_PROFIT_CASH_BACK_WAIT, PRESENT_PROFIT_CASH_BACK_DONE, "保证金已回填", null),
    APPLY(null, PRESENT_PROFIT_APPLY, "申请登记", null),
    MARKET_AUDIT_PROCESSING(PRESENT_PROFIT_APPLY, PRESENT_PROFIT_AUDIT_PROCESSING, "市场方审核中", null),
    MARKET_AUDIT_REJECT(PRESENT_PROFIT_AUDIT_PROCESSING, PRESENT_PROFIT_AUDIT_REJECT, "市场方审核退回", null),
    BANK_PROCESSING(PRESENT_PROFIT_AUDIT_APPROVED, PRESENT_PROFIT_BANK_PROCESSING, "银行处理中", null),
    MARKET_AUDIT_APPROVED(PRESENT_PROFIT_AUDIT_PROCESSING, PRESENT_PROFIT_AUDIT_APPROVED, "市场方审核通过", BANK_PROCESSING),
    BANK_SUCCESS(PRESENT_PROFIT_BANK_PROCESSING, PRESENT_PROFIT_BANK_SUCCESS, "银行处理成功", null),
    COST_PAY(PRESENT_PROFIT_APPLY, PRESENT_PROFIT_BANK_PROCESSING, "费用支付", null),
    BANK_FAIL(PRESENT_PROFIT_BANK_PROCESSING, PRESENT_PROFIT_BANK_FAIL, "银行处理失败", null);

    private String sourceStatus; // 源状态
    private String targetStatus; // 目标状态
    private String description; // 事件描述
    private ProfitPressentStatusChangeEvent postEvent; // 后置事件

    ProfitPressentStatusChangeEvent(String sourceStatus, String targetStatus, String description, ProfitPressentStatusChangeEvent postEvent) {
        this.sourceStatus = sourceStatus;
        this.targetStatus = targetStatus;
        this.description = description;
        this.postEvent = postEvent;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public String getDescription() {
        return description;
    }

    public ProfitPressentStatusChangeEvent getPostEvent() {
        return postEvent;
    }
}
