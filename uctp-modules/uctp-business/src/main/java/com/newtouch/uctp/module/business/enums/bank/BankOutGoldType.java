package com.newtouch.uctp.module.business.enums.bank;

public enum BankOutGoldType {

    CHILD_ACCT_OUT_GOLD("06", "按子账户出金");


    private String code;

    private String value;

    BankOutGoldType(String code, String value) {
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
