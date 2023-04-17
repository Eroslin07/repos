package com.newtouch.uctp.framework.qiyuesuo.core.client;

import com.newtouch.uctp.framework.common.exception.ErrorCode;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoFrameworkErrorCodeConstants;

import java.util.function.Function;

/**
 * 将 API 的错误码，转换为通用的错误码
 *
 * @see QiyuesuoCommonResult
 * @see QiyuesuoFrameworkErrorCodeConstants
 *
 * @author lc
 */
public interface QiyuesuoCodeMapping extends Function<String, ErrorCode> {
}
