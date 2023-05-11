package com.newtouch.uctp.module.business.enums.bank;

/**
 * 入金通知返回码
 */
public enum NotificationResultCode {

    RESULT_RETURN_CODE("000000000000", "接受正常-响应体"),
    RESULT_STATUS_CODE("00", "接受正常-响应头");

    private String code;

    private String value;

    NotificationResultCode(String code, String value) {
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
