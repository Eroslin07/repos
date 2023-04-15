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
    ErrorCode CAR_INFO_IS_EXISTS = new ErrorCode(1005000014, "该车辆信息已存在草稿中");
    ErrorCode CAR_INFO_DETAILS_NOT_EXISTS = new ErrorCode(1005000012, "车辆明细不存在");
    ErrorCode CAR_INFO_SELL_AMOUNT_ERROR = new ErrorCode(1005000013, "卖车金额必须大于收车金额");
    ErrorCode CAR_INFO_STATUS_ERROR = new ErrorCode(1005000014, "车辆状态异常");


    // ========== business 契约锁错误 1005000100 ==========
    ErrorCode QYS_INIT_PARAM_ERROR = new ErrorCode(1005000100, "初始化契约锁的必要参数不存在");
    ErrorCode QYS_CLIENT_INIT_PARAM_ERROR = new ErrorCode(1005000100, "契约锁client为空");
    ErrorCode QYS_CONTRACTID_NOT_EXISTS = new ErrorCode(1005000100, "契约锁必要参数[contractId]不存在");
    ErrorCode QYS_BUSINESS_TYPE_NOT_EXISTS = new ErrorCode(1005000100, "契约锁业务分类不存在");
    ErrorCode QYS_FILE_URL_NOT_NULL = new ErrorCode(1005000100, "契约锁:输出路径不能为空");

    // ========== business 资金错误 1005000200 ==========
    ErrorCode ACC_ACCOUNT_NO_NOT_NULL = new ErrorCode(1005000201, "商户号不能为空");
    ErrorCode ACC_CONTRACT_NO_NOT_NULL = new ErrorCode(1005000202, "交易合同号不能为空");
    ErrorCode ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL = new ErrorCode(1005000203, "收车金额不能为空");
    ErrorCode ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO = new ErrorCode(1005000204, "收车金额不能为负");
    ErrorCode ACC_CAR_SALES_AMOUNT_NOT_NULL = new ErrorCode(1005000205, "卖车金额不能为空");
    ErrorCode ACC_CAR_SALES_AMOUNT_LESS_THAN_ZERO = new ErrorCode(1005000206, "卖车金额不能为负");
    ErrorCode ACC_COST_TYPE_REPEAT = new ErrorCode(1005000207, "费用类型不能重复");
    ErrorCode ACC_TAX_TYPE_REPEAT = new ErrorCode(1005000208, "税费类型不能重复");
    ErrorCode ACC_MERCHANT_ACCOUNT_NOT_EXISTS = new ErrorCode(1005000209, "商户账户不存在");
}
