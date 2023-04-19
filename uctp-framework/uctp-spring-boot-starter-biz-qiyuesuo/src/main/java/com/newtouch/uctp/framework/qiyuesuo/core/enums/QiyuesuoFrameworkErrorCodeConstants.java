package com.newtouch.uctp.framework.qiyuesuo.core.enums;

import com.newtouch.uctp.framework.common.exception.ErrorCode;

/**
 * 契约锁框架的错误码枚举
 *
 * 短信框架，使用 2-003-000-000 段
 *
 * @author lc
 */
public interface QiyuesuoFrameworkErrorCodeConstants {

    ErrorCode EXCEPTION = new ErrorCode(2003000999, "调用异常");
    ErrorCode QIYUESUO_UNKNOWN = new ErrorCode(2003000001, "ERROR，服务器内部错误");

}
