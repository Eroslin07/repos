package com.newtouch.uctp.module.business.enums;

import java.util.Objects;

public enum TradeTypeEnum {

    TRADE_TYPE_RECHARGE("1", "保证金-充值"),
    TRADE_TYPE_BACK("2", "保证金-回填"),
    TRADE_TYPE_WITHDRAW("3", "保证金-提现"),
    TRADE_TYPE_DEDUCTION("4", "保证金-扣减"),
    TRADE_TYPE_PREEMPTION("5", "保证金-预占"),
    TRADE_TYPE_RELEASE("6", "保证金-释放");

    private String type;
    private String name;

    TradeTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getTradeTypeName(String type) {
        TradeTypeEnum[] values = TradeTypeEnum.values();
        for (TradeTypeEnum value : values) {
            if (Objects.equals(value.type, type)) {
                return value.name;
            }
        }
        return null;
    }
}
