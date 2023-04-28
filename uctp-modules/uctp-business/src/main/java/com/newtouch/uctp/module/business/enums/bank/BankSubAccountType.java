package com.newtouch.uctp.module.business.enums.bank;

/**
 * 银行子账户类型
 */
public enum BankSubAccountType {
    WAITTING_CLEAR("1001", "待清分子账户"),
    WAITTING_TAX("1002", "待缴税费子账户"),
    WAITTING_MARKET_REVENUE("1003", "市场收益子账户"),
    WAITTING_SERVICE("1004", "平台服务费子账户"),
    WAITTING_MERCHANT_CASH("2001", "商户保证金子账户"),
    WAITTING_MERCHANT_PROFIT("2002", "商户利润子账户");

    private final String code;
    private final String text;

    BankSubAccountType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
