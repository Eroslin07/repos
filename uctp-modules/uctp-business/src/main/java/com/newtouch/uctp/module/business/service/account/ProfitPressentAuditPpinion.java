package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.module.business.service.account.event.ProfitPressentStatusChangeEvent;

public enum ProfitPressentAuditPpinion {
    // 审核中
    AUDIT_PROCESSING(ProfitPressentStatusChangeEvent.MARKET_AUDIT_PROCESSING),
    // 审核退回
    AUDIT_REJECT(ProfitPressentStatusChangeEvent.MARKET_AUDIT_REJECT),
    // 审核通过
    AUDIT_APPROVED(ProfitPressentStatusChangeEvent.MARKET_AUDIT_APPROVED);

    private ProfitPressentStatusChangeEvent event;

    ProfitPressentAuditPpinion(ProfitPressentStatusChangeEvent event) {
        this.event = event;
    }

    public ProfitPressentStatusChangeEvent getEvent() {
        return event;
    }
}
