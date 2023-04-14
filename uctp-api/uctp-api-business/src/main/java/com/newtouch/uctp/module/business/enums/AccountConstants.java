package com.newtouch.uctp.module.business.enums;

public class AccountConstants {

    //交易类型：保证金-充值
    public static final String TRADE_TYPE_RECHARGE = "1";
    //交易类型：保证金-回填
    public static final String TRADE_TYPE_BACK = "2";
    //交易类型：保证金-提现
    public static final String TRADE_TYPE_WITHDRAW = "3";
    //交易类型：保证金-扣减
    public static final String TRADE_TYPE_DEDUCTION = "4";
    //交易类型：保证金-预占
    public static final String TRADE_TYPE_PREEMPTION = "5";
    //交易类型：保证金-释放
    public static final String TRADE_TYPE_RELEASE = "6";

    //损益类型-收入
    public static final String PROFIT_LOSS_TYPE_INCOME = "1";
    //损益类型-支出
    public static final String PROFIT_LOSS_TYPE_DISBURSEMENT = "2";

    //默认支付渠道-兴业银行
    public static final String DEFAULT_PAY_CHANNEL = "1";

    //交易去向-收车
    public static final String TRADE_TO_BUY_CARS = "1";
    //交易去向-我的保证金
    public static final String TRADE_TO_MY_CASH = "2";

    //客户类型-商户
    public static final String CUST_TYPE_MERCHANT = "1";
    //客户类型-买车
    public static final String CUST_TYPE_BUYER = "2";
    //客户类型-卖车
    public static final String CUST_TYPE_SELLER = "3";


    //交易状态-待支付
    public static final String TRAN_STATE_WAIT = "1";
    //交易状态-支付中
    public static final String TRAN_STATE_DURING = "2";
    //交易状态-支付成功
    public static final String TRAN_STATE_SUCCESS = "3";
    //交易状态-支付失败
    public static final String TRAN_STATE_FAIL = "4";

    public static Integer ERROR_CODE_ACCOUNT_NOT_FOUND = 90010001;
    public static String ERROR_MESSAGE_ACCOUNT_NOT_FOUND = "未查询到当前账号信息";

    public static Integer ERROR_CODE_REVERSION_ERROR = 90010002;
    public static String ERROR_MESSAGE_REVERSION_ERROR = "版本号错误，请刷新页面后重试。";

    public static Integer ERROR_CODE_INTERFACE_CALL_FAILURE = 90010003;
    public static String ERROR_MESSAGE_INTERFACE_CALL_FAILURE = "接口调用失败。";

    public static Integer ERROR_CODE_INSUFFICIENT_AVAILABLE_BALANCE = 90010004;
    public static String ERROR_MESSAGE_INSUFFICIENT_AVAILABLE_BALANCE = "当前账户可用余额不足。";

}
