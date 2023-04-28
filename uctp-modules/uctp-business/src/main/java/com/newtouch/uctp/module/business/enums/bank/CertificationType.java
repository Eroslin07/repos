package com.newtouch.uctp.module.business.enums.bank;

/**
 * 银行证件类型枚举
 */
public enum CertificationType {

    ID_CARD("1","居民身份证"),
    BUSINESS_LICENSE("2010","营业执照");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    CertificationType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
