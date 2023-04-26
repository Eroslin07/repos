package com.newtouch.uctp.module.business.enums.bank;

/**
 * 清分状态
 */
public enum ClearingState {
    CLEARING_FAILURE("00", "清分失败"),
    TO_BE_CLEARED("01", "待清分"),
    CLEARING_PROCESSING("02", "清分中"),
    CLEARING_SUCCESS("03", "清分成功");

    private String code;

    private String value;

    ClearingState(String code, String value) {
        this.code = code;
        this.value = value;
    }
}