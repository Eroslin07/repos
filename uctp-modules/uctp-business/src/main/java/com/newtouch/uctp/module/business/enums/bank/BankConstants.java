package com.newtouch.uctp.module.business.enums.bank;

public class BankConstants {

    // 请求URL地址前缀，后续配置文件设置测试与生产
    private final static String API_URL = "https://etest4.spdb.com.cn/spdb/uat";

    // 市场方母账号
    private final static String ACCT_NO = "";
    // 交易地区代码 TODO 银行确认
    private final static String AREA_CODE = "";


    // 子账号互转API
    private final static String INNER_TRANSFER_API = "/api/fundDeposits/eDeposits/innerTransfer";
    // 不明入金清分API
    private final static String UNKNOWN_CLEARINGS_API = "/api/fundDeposits/eDeposits/unknownClearings";
    // 按余额出金API
    private final static String BALANCES_WITHDRAWALS_API = "/api/fundDeposits/eDeposits/Withdrawals/balances";
    // 虚拟账号生成API
    private final static String NOMINAL_ACCOUNT_API = "/api/fundDeposits/eDeposits/nominalAcccount";
    // 订单支付API
    private final static String ORDERS_PAY_API = "/api/acquiring/orders/pay";
    // 订单支付结果查询API
    private final static String ORDERS_PAY_STATUS = "/api/acquiring/orders/payStatus";
}
