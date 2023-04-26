package com.newtouch.uctp.module.business.enums.bank;

/**
 * 清分类型
 */
public enum ClearingType {
    DEPOSITS_BY_SUB_ACCOUNT("03", "按子账号入金"),
    ORIGINAL_WAY_BACK("05", "原路退回");

    private String code;

    private String value;

    ClearingType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}