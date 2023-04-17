package com.newtouch.uctp.module.business.enums;

public class AccountConstants {

    //交易类型：保证金-充值
    public static final String TRADE_TYPE_RECHARGE = "10011001";
    //交易类型：保证金-卖车款回填
    public static final String TRADE_TYPE_BACK = "10011002";
    //交易类型：保证金-利润回填
    public static final String TRADE_TYPE_PROFIT_BACK = "10011003";
    //交易类型：保证金-提现
    public static final String TRADE_TYPE_WITHDRAW = "10011004";
    //交易类型：保证金-提现中
    public static final String TRADE_TYPE_WITHDRAWING = "10011005";
    //交易类型：保证金-扣减
    public static final String TRADE_TYPE_DEDUCTION = "10011006";
    //交易类型：保证金-预占
    public static final String TRADE_TYPE_PREEMPTION = "10011007";
    //交易类型：保证金-释放
    public static final String TRADE_TYPE_RELEASE = "10011008";

    //损益类型-收入
    public static final String PROFIT_LOSS_TYPE_INCOME = "10021001";
    //损益类型-支出
    public static final String PROFIT_LOSS_TYPE_DISBURSEMENT = "10021002";

    //支付渠道-兴业银行
    public static final String PAY_CHANNEL_DEFAULT = "10031001";
    //支付渠道-平台
    public static final String PAY_CHANNEL_PLATFORM= "10031002";

    //交易去向-收车
    public static final String TRADE_TO_BUY_CARS = "10041001";
    //交易去向-我的保证金
    public static final String TRADE_TO_MY_CASH = "10041002";
    //交易去向-市场方
    public static final String TRADE_TO_MARKET = "10041003";
    //交易去向-我的利润
    public static final String TRADE_TO_MY_PROFIT = "10041004";

    //客户类型-商户
    public static final String CUST_TYPE_MERCHANT = "10051001";
    //客户类型-买车
    public static final String CUST_TYPE_BUYER = "10051002";
    //客户类型-卖车
    public static final String CUST_TYPE_SELLER = "10051003";


    //交易状态-待支付
    public static final String TRAN_STATE_WAIT = "10061001";
    //交易状态-支付中
    public static final String TRAN_STATE_DURING = "10061002";
    //交易状态-支付成功
    public static final String TRAN_STATE_SUCCESS = "10061003";
    //交易状态-支付失败
    public static final String TRAN_STATE_FAIL = "10061004";

    // 提现类型C：保证金提现P：利润提现
    public static final String PRESENT_TYPE_CASH = "C";
    public static final String PRESENT_TYPE_PROFIT = "P";

    public static Integer ERROR_CODE_ACCOUNT_NOT_FOUND = 90010001;
    public static String ERROR_MESSAGE_ACCOUNT_NOT_FOUND = "未查询到当前账号信息";

    public static Integer ERROR_CODE_REVERSION_ERROR = 90010002;
    public static String ERROR_MESSAGE_REVERSION_ERROR = "版本号错误，请刷新页面后重试。";

    public static Integer ERROR_CODE_INTERFACE_CALL_FAILURE = 90010003;
    public static String ERROR_MESSAGE_INTERFACE_CALL_FAILURE = "接口调用失败。";

    public static Integer ERROR_CODE_INSUFFICIENT_AVAILABLE_BALANCE = 90010004;
    public static String ERROR_MESSAGE_INSUFFICIENT_AVAILABLE_BALANCE = "当前账户可用余额不足。";

    public static Integer ERROR_CODE_CONTRACT_NO_NOT_FOUND = 90010005;
    public static String ERROR_MESSAGE_CONTRACT_NO_NOT_FOUND = "未查询到当前合同号冻结流水明细。";

    public static Integer ERROR_CODE_INSUFFICIENT_FREEZE_CASH = 90010006;
    public static String ERROR_MESSAGE_INSUFFICIENT_FREEZE_CASH = "当前账户冻结金额不足。";


    // 保证金提现状态-发起提现申请
    public static final String PRESENT_STATUS_CASH_APPLY = "10081001";
    // 保证金提现状态-银行处理中
    public static final String PRESENT_STATUS_CASH_PROCESSING = "10081002";
    // 保证金提现状态-到账成功
    public static final String PRESENT_STATUS_CASH_SUCCESS = "10081003";

    // 保证金提现状态-保证金提现中
    public static final String PRESENT_CASH_PROCESSING = "10091001";
    // 保证金提现状态-保证金提现
    public static final String PRESENT_CASH_SUCCESS = "10091002";
}
