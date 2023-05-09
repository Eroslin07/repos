package com.newtouch.uctp.framework.common.exception;

import com.newtouch.uctp.framework.common.exception.enums.ServiceErrorCodeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 银行接口交互异常 Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BankException extends RuntimeException {

    /**
     * 业务错误码
     *
     * @see ServiceErrorCodeRange
     */
    private Integer code;

    /**
     * 请求报文
     */
    private String request;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     *
     * @param message
     */
    public BankException(String message) {
    }

    public BankException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public BankException(Integer code, String message) {
        this(code, "", message);
    }

    public BankException(String request, String message) {
        this(0, request, message);
    }

    public BankException(Integer code, String request, String message) {
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public Integer getCode() {
        return code;
    }

    public BankException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BankException setMessage(String message) {
        this.message = message;
        return this;
    }

}
