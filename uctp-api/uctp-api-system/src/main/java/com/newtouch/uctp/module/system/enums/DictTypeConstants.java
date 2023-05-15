package com.newtouch.uctp.module.system.enums;

/**
 * System 字典类型的枚举类
 *
 * @author 芋道源码
 */
public interface DictTypeConstants {

    String USER_TYPE = "user_type"; // 用户类型
    String COMMON_STATUS = "common_status"; // 系统状态

    // ========== SYSTEM 模块 ==========

    String USER_SEX = "system_user_sex"; // 用户性别

    String OPERATE_TYPE = "system_operate_type"; // 操作类型

    String LOGIN_TYPE = "system_login_type"; // 登录日志的类型
    String LOGIN_RESULT = "system_login_result"; // 登录结果

    String ERROR_CODE_TYPE = "system_error_code_type"; // 错误码的类型枚举

    String SMS_CHANNEL_CODE = "system_sms_channel_code"; // 短信渠道编码
    String SMS_TEMPLATE_TYPE = "system_sms_template_type"; // 短信模板类型
    String SMS_SEND_STATUS = "system_sms_send_status"; // 短信发送状态
    String SMS_RECEIVE_STATUS = "system_sms_receive_status"; // 短信接收状态


    // ========== business 模块 ==========
    String CAR_EXPENSE_CONFIG_DEFAULT = "car_expense_config_default"; // 默认车辆金额费用配置项

    String CAR_TAX_VALUE_ADDED = "tax_value_added"; // 增值税率
    String CAR_SERVICE_OPERATION = "service_operation"; // 运营服务费
    String CAR_SERVICE_TRANSFER_SELL = "service_transfer_sell"; // 过户服务费(卖车)
    String CAR_SERVICE_TRANSFER_BUY = "service_transfer_buy"; // 过户服务费(收车)

    String SETTLEMENT_INVOICE_STATUS = "Invoice_status"; //发票状态
    String SETTLEMENT_INVOICE_TYPE = "Invoice_type"; //发票类型
    String TAX_TYPE = "tax_type"; //税费类型
    String COST_TYPE = "cost_type"; //费用类型


}
