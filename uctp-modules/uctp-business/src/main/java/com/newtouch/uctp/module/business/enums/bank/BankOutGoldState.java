package com.newtouch.uctp.module.business.enums.bank;

/**
 * 出金状态码
 */
public enum BankOutGoldState {

    OUT_GOLD_FAILED("00", "出金失败"),
    OUT_GOLD_PROCESSING("01", "处理中"),
    OUT_GOLD_SUCCESS("02", "出金成功");

    private String code;

    private String value;

    BankOutGoldState(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
