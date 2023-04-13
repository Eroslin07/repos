package com.newtouch.uctp.module.business.enums;

import com.newtouch.uctp.framework.common.exception.ErrorCode;

/**
 * Business 错误码枚举类
 *
 * business 系统，使用 1-005-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== business 模块 1005000000 ==========
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1005000000, "二手车交易平台业务异常");
    ErrorCode CAR_INFO_NOT_EXISTS = new ErrorCode(1005000011, "车辆不存在");
    ErrorCode CAR_INFO_DETAILS_NOT_EXISTS = new ErrorCode(1005000012, "车辆明细不存在");
    ErrorCode CAR_INFO_SELL_AMOUNT_ERROR = new ErrorCode(1005000013, "卖车金额必须大于收车金额");
    ErrorCode CAR_INFO_STATUS_ERROR = new ErrorCode(1005000014, "车辆状态异常");


    // ========== business 契约锁错误 1005000100 ==========
    ErrorCode QYS_INIT_PARAM_ERROR = new ErrorCode(1005000100, "初始化契约锁的必要参数不存在");
    ErrorCode QYS_CLIENT_INIT_PARAM_ERROR = new ErrorCode(1005000100, "契约锁client为空");
    ErrorCode QYS_CONTRACTID_NOT_EXISTS = new ErrorCode(1005000100, "契约锁必要参数[contractId]不存在");
    ErrorCode QYS_BUSINESS_TYPE_NOT_EXISTS = new ErrorCode(1005000100, "契约锁业务分类不存在");
    ErrorCode QYS_FILE_URL_NOT_NULL = new ErrorCode(1005000100, "契约锁:输出路径不能为空");
}
