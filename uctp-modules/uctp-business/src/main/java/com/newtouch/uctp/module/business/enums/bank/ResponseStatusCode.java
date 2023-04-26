package com.newtouch.uctp.module.business.enums.bank;

/**
 * 银行接口响应状态码
 */
public enum ResponseStatusCode {

    TRAN_SUCCESS("0000", "交易成功"),
    TRAN_PROCESSING("1000", "交易处理中"),
    UNKNOWN_ERROR("0001", "未知错误"),
    PARAM_VALID_FAILED("0002", "参数校验失败"),
    ASYNC_TRAN_FAILED("0003", "异步交易失败");

    private String code;

    private String value;

    ResponseStatusCode(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
