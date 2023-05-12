package com.newtouch.uctp.module.business.enums.bank;

import cn.hutool.core.util.NumberUtil;

import java.util.function.Function;

/**
 * 金额单位
 */
public enum MonetaryUnit {
    /**
     * 分单位
     */
    CENT("分", (amount) -> {
        return NumberUtil.div(amount, "100", 2).toString();
    }),
    /**
     * 角单位
     */
    TEN_CENT("角", (amount) -> {
        return NumberUtil.div(amount, "10", 1).toString();
    });


    private String unit;

    private Function<String, String> service;

    MonetaryUnit(String unit, Function<String, String> service) {
        this.unit = unit;
        this.service = service;
    }

    public String get(String amount) {
        return this.service.apply(amount);
    }
}
