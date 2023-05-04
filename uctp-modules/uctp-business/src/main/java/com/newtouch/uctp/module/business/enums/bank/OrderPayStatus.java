package com.newtouch.uctp.module.business.enums.bank;

/**
 * 订单支付响应交易状态
 */
public enum OrderPayStatus {
    ORDER_ACCEPTED_SUCCESS("00", "订单受理成功"),
    DUPLICATE_ORDER_PAYMENT("01", "订单支付重复"),
    ORDER_ACCEPTED_FAILURE("02", "订单受理失败");

    private String code;

    private String value;

    OrderPayStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
