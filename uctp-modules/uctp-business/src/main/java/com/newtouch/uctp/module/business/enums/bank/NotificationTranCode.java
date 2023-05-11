package com.newtouch.uctp.module.business.enums.bank;

/**
 * 入金通知交易码
 */
public enum NotificationTranCode {

    NOTICE_TYPE("A1007", "客户入金通知");


    private String code;

    private String value;

    NotificationTranCode(String code, String value) {
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
