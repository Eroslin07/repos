package com.newtouch.uctp.module.business.enums.bank;

/**
 * 清分方式
 */
public enum ClearingMode {
    AUTOMATIC_CLEARING("00", "自动清分"),
    REVIEWED_AND_CLEARED("01", "待审核清分");

    private String code;

    private String value;

    ClearingMode(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
