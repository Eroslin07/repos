package com.newtouch.uctp.module.business.enums;

import java.util.stream.Stream;

/**
 * 车辆相关状态，也可参考原型图和字典表
 *
 */
public enum CarStatus {
    //--------------收车中-----------------------
    COLLECT(1,"收车中"),
    COLLECT_A(11,"草稿"),
    COLLECT_A_A(111,"草稿"),

    COLLECT_B(12,"合同已发起"),
    COLLECT_B_A(121,"公允审核"),
    COLLECT_B_B(122,"委托合同已发起"),
    COLLECT_B_C(123,"合同已发起"),
    COLLECT_B_D(124,"待支付"),


    COLLECT_C(13,"支付失败"),
    COLLECT_C_A(131,"支付失败"),

    //--------------待售中-----------------------
    SALE(2,"待售中"),
    SALE_A(21,"待过户"),
    SALE_A_A(125,"待开票"),
    SALE_A_B(211,"待过户"),

    SALE_B(22,"未检测"),
    SALE_B_A(221,"未检测"),

    SALE_C(23,"已检测"),
    SALE_C_A(231,"已检测"),
    //--------------卖车中-----------------------
    SELL(3,"卖车中"),
    SELL_A(31,"草稿"),
    SELL_A_A(311,"草稿"),

    SELL_B(32,"合同已发起"),
    SELL_B_A(321,"公允审核"),
    SELL_B_B(322,"委托合同已发起"),
    SELL_B_C(323,"合同已发起"),

    SELL_C(33,"待支付"),
    SELL_C_A(331,"待支付"),
    //--------------已售出-----------------------
    SOLD(4,"已售出"),
    SOLD_A(41,"待过户"),
    SOLD_A_A(411,"待开票"),
    SOLD_A_B(411,"待过户"),

    SOLD_B(42,"待分账"),
    SOLD_B_A(421,"待分账"),

    SOLD_C(43,"已分账"),
    SOLD_C_A(431,"已分账");



    private int value;
    private String text;

    CarStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int value(){
        return this.value;
    }
    public String text(){
        return this.text;
    }

    public static CarStatus toType(int value){
        return Stream.of(CarStatus.values())
                .filter(c -> c.value == value)
                .findAny()
                .orElse(null);
    }
}
