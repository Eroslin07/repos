package com.newtouch.uctp.module.business.enums.bank;

public class BankConstants {

    // 请求URL地址前缀，后续配置文件设置测试与生产
    public final static String API_URL = "https://etest4.spdb.com.cn/spdb/uat";

    public final static String tranDateFormat = "yyyyMMdd";
    public final static String tranTimeFormat = "HHmmss";

    /**
     * 银行合作商户号 todo 合共银行评审后提供
     */
    public final static String MERCHANT_ID = "";

    // 市场方母账号
    public final static String ACCT_NO = "88010078801200000403";
    // 交易地区代码
    public final static String AREA_CODE = "tyesc001";
    // 母账号名称
    public final static String ACCT_NAME = "自动化测试438114";
    // 充值保证金默认开户行
    public final static String CASH_OPEN_BANK_NAME = "浦发银行";


    // 子账号互转API
    public final static String INNER_TRANSFER_API = "/api/fundDeposits/eDeposits/innerTransfer";
    // 不明入金清分API
    public final static String UNKNOWN_CLEARINGS_API = "/api/fundDeposits/eDeposits/unknownClearings";
    // 按余额出金API
    public final static String BALANCES_WITHDRAWALS_API = "/api/fundDeposits/eDeposits/Withdrawals/balances";
    // 虚拟账号生成API
    public final static String NOMINAL_ACCOUNT_API = "/api/fundDeposits/eDeposits/nominalAcccount";
    // 订单支付API
    public final static String ORDERS_PAY_API = "/api/acquiring/orders/pay";
    // 订单支付结果查询API
    public final static String ORDERS_PAY_STATUS = "/api/acquiring/orders/payStatus";
    // 场景转账链接生成交易
    public final static String TECH_ADDRESS_API = "/api/tech/addresses";


    public final static String REQUEST_METHOD_POST = "POST";
    public final static String REQUEST_METHOD_GET = "GET";

    public final static int BANK_STATUS_CODE_SUCCESS = 200;
}
