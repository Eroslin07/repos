package com.newtouch.uctp.module.business.enums;

/**
 * 费用常量信息
 */
public class ExpenseConstants {

    /**
     * 平台服务费用 单位为分
     * 用于利润查询展示其中包含 IT_SERVICE_FEE平台IT服务费用
     */
    public static final Long PLATFORM_SERVICE_FEE = 20000L;

    /**
     * 平台服务费用 字符额度
     * 用于利润查询展示其中包含 IT_SERVICE_FEE平台IT服务费用
     */
    public static final String PLATFORM_SERVICE_FEE_STR = "200.00";

    /**
     * 平台IT服务费用 单位为分
     * 后续平台用于银行转账时使用的额度
     */
    public static final Long IT_SERVICE_FEE = 5000L;

    /**
     * 平台IT服务费用 字符额度
     * 后续平台用于银行转账时使用的额度
     */
    public static final String IT_SERVICE_FEE_STR = "50.00";

    /**
     * 税率
     * 后续平台用于银行转账时使用的额度
     */
    public static final Double TAX_RATE = 0.005D;
}
