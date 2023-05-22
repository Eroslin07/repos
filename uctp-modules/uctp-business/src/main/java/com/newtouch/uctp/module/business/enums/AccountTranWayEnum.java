package com.newtouch.uctp.module.business.enums;

/**
 * 商户虚拟账户交易类型
 */
public enum AccountTranWayEnum {

    ONLINE(1, "线上交易"),
    OFFLINE(2, "线下交易");

    private Integer code;

    private String value;

    AccountTranWayEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


}
