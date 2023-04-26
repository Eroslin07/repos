package com.newtouch.uctp.module.business.enums.bank;

/**
 * 订单支付交易状态码
 */
public enum TranStatusCode {
    TRAN_SUCCESS("00", "交易成功"),
    TRAN_FAILURE("01", "交易失败"),
    PARTIAL_RETURN("03", "部分退货"),
    ALL_RETURN("04", "全部退货"),
    RETURN_PROCESSING("05", "退货中"),
    IN_PAYMENT("09", "支付中"),
    REVOKED("10", "已撤销"),
    CORRECTED("11", "已冲正"),
    TIME_OUT("99", "超时");

    private String code;

    private String value;

    TranStatusCode(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
