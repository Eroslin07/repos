package com.newtouch.uctp.module.business.enums;

import java.util.stream.Stream;

/**
 * 契约锁回调类型
 */
public enum QysCallBackType {
    COMPANY_AUTH(1,"企业认证"),
    AUTH_RETURN(2,"授权返回值"),
    PRIVATIZED(3,"申请私有化应用返回值"),
    CONTRACT_STATUS(4,"合同状态回调返回值"),
    SIGNATURE(5,"印章授权静默签返回值"),
    PERSONAL_AUTH(6,"个人认证返回值"),
    PERSONAL_SIGNATURE_AUTH(7,"个人签名授权返回值");


    private int value;
    private String text;


    QysCallBackType(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int value(){
        return this.value;
    }
    public String text(){
        return this.text;
    }

    public static QysCallBackType toType(int value){
        return Stream.of(QysCallBackType.values())
                .filter(c -> c.value == value)
                .findAny()
                .orElse(null);
    }
}
