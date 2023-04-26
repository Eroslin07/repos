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
    ErrorCode QYS_INIT_PARAM_ERROR = new ErrorCode(1005000101, "初始化契约锁的必要参数不存在");
    ErrorCode QYS_CLIENT_INIT_PARAM_ERROR = new ErrorCode(1005000102, "契约锁client为空");
    ErrorCode QYS_CONTRACTID_NOT_EXISTS = new ErrorCode(1005000103, "契约锁必要参数[contractId]不存在");
    ErrorCode QYS_BUSINESS_TYPE_NOT_EXISTS = new ErrorCode(1005000104, "契约锁业务分类不存在");
    ErrorCode QYS_FILE_URL_NOT_NULL = new ErrorCode(1005000105, "契约锁:输出路径不能为空");
    ErrorCode QYS_CONFIG_NOT_EXISTS = new ErrorCode(1005000106, "契约锁不存在");
    ErrorCode QYS_CONFIG_AUTH_ERROR = new ErrorCode(1005000107, "契约锁未做认证授权");
    ErrorCode QYS_CALLBACK_NOT_EXISTS = new ErrorCode(1005000150, "契约锁回调日志不存在");
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
    ErrorCode ACC_PRESENT_ERROR = new ErrorCode(1005000210, "提现处理错误");
    ErrorCode ACC_PRESENT_PROFIT_INSUFFICIENT = new ErrorCode(1005000211, "利润余额不足");
    ErrorCode ACC_PRESENT_PROFIT_BANK_NOT_NULL = new ErrorCode(1005000212, "银行账户不能为空");
    ErrorCode ACC_PRESENT_PROFIT_RECORDED_ERROR = new ErrorCode(1005000213, "利润划入出错");
    ErrorCode ACC_PRESENT_PROFIT_RECORDED_REPEAT = new ErrorCode(1005000214, "利润重复划入");

    ErrorCode USERS_INFO_ERROR = new ErrorCode(1005000215, "用户信息不存在");
    ErrorCode DEPT_INFO_ERROR = new ErrorCode(1005000216, "部门信息不存在");


    ErrorCode FILL_ERROR = new ErrorCode(1005000216, "系统文件表对应信息不存在");
    // ========== business 合同错误 1005000300 ==========
    ErrorCode CONTRACT_NOT_EXISTS = new ErrorCode(1005000300, "合同不存在");
    ErrorCode CONTRACT_TYPE_UNKNOWN = new ErrorCode(1005000301, "未知合同类型");


}
