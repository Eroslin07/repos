package com.newtouch.uctp.module.business.enums;

import java.util.stream.Stream;

/**
 * 契约锁合同状态
 *
 */
public enum QysContractStatus {
    DRAFT("DRAFT","草稿"),
    RECALLED("RECALLED","已撤回"),
    SIGNING("SIGNING","签署中"),
    REJECTED("REJECTED","已退回"),
    COMPLETE("COMPLETE","已完成"),
    EXPIRED("EXPIRED","已过期"),
    FILLING("FILLING","拟定中"),
    FAILED("FAILED","签署失败"),
    INVALIDING("INVALIDING","作废中"),
    END("END","强制结束"),
    INVALIDED("INVALIDED","已作废");

    private String value;
    private String text;

    QysContractStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String value(){
        return this.value;
    }
    public String text(){
        return this.text;
    }

    public static QysContractStatus toType(String value){
        return Stream.of(QysContractStatus.values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }
}
