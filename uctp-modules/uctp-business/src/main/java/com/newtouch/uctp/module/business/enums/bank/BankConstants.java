package com.newtouch.uctp.module.business.enums.bank;

public class BankConstants {

    // 市场方母账号
    public final static String ACCT_NO = "41141221013002507487";

    // 母账号名称
    public final static String ACCT_NAME = "山西车友通汽车销售有限公司";

    // 开户行名称
    public final static String OPEN_BRANCH_NAME = "交行新建南路支行";


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

    /**
     * 银行交易币种代码
     */
    public final static String CURRENCY_CODE = "CNY";

    /**
     * 企业代码 todo bank
     */
    public final static String CORP_NO = "";

    /**
     * 企业用户号 todo bank
     */
    public final static String USER_NO = "";


}
