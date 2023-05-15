package com.newtouch.uctp.module.business.enums.bank;

/**
 * 交易对账状态
 */
public enum TranApprove {

    UNRECONCILED("0", "未对账"),

    RECONCILED("1", "已对账"),

    RECONCILED_FAILED("2", "对账失败");

    private String code;

    private String value;

    TranApprove(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
