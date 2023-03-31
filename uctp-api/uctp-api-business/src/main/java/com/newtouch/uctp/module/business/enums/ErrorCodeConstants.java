package com.newtouch.uctp.module.business.enums;

import com.newtouch.uctp.framework.common.exception.ErrorCode;

/**
 * Business 错误码枚举类
 *
 * business 系统，使用 1-005-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== AUTH 模块 1003000000 ==========
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1005000000, "二手车交易平台业务异常");
    ErrorCode CAR_INFO_NOT_EXISTS = new ErrorCode(1005000001, "车辆主不存在");

}
