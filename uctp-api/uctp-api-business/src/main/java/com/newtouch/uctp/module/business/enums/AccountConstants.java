package com.newtouch.uctp.module.business.enums;

public class AccountConstants {

    //交易类型：保证金-充值
    public static final String TRADE_TYPE_RECHARGE = "保证金-充值";
    //交易类型：保证金-回填
    public static final String TRADE_TYPE_BACK = "保证金-回填";
    //交易类型：保证金-利润回填
    public static final String TRADE_TYPE_PROFIT_BACK = "保证金-利润回填";
    //交易类型：保证金-提现
    public static final String TRADE_TYPE_WITHDRAW = "保证金-提现";
    //交易类型：保证金-扣减
    public static final String TRADE_TYPE_DEDUCTION = "保证金-扣减";
    //交易类型：保证金-预占
    public static final String TRADE_TYPE_PREEMPTION = "保证金-预占";
    //交易类型：保证金-释放
    public static final String TRADE_TYPE_RELEASE = "保证金-释放";

    //损益类型-收入
    public static final String PROFIT_LOSS_TYPE_INCOME = "收入";
    //损益类型-支出
    public static final String PROFIT_LOSS_TYPE_DISBURSEMENT = "支出";

    //支付渠道-兴业银行
    public static final String PAY_CHANNEL_DEFAULT = "兴业银行";
    //支付渠道-平台
    public static final String PAY_CHANNEL_PLATFORM= "平台";

    //交易去向-收车
    public static final String TRADE_TO_BUY_CARS = "收车";
    //交易去向-我的保证金
    public static final String TRADE_TO_MY_CASH = "我的保证金";
    //交易去向-市场方
    public static final String TRADE_TO_MARKET = "3";
    //交易去向-我的利润
    public static final String TRADE_TO_MY_PROFIT = "4";

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

    // 利润：交易类型
    //卖车利润
    public static final String TRAN_PROFIT_SALES_PROFIT = "1";
    //利润提现
    public static final String TRAN_PROFIT_PRESENT = "2";
    //利润-保证金回填
    public static final String TRAN_PROFIT_CASH_BACK = "3";
    //利润-服务费
    public static final String TRAN_PROFIT_SERVICE_COST = "4";
    //利润-税费
    public static final String TRAN_PROFIT_TAX_COST = "5";

    // 利润：提现状态
    //待回填保证金
    public static final String PRESENT_PROFIT_CASH_BACK_WAIT = "1";
    //已回填保证金
    public static final String PRESENT_PROFIT_CASH_BACK_DONE = "2";
    //申请登记
    public static final String PRESENT_PROFIT_APPLY = "0";
    //市场方退回
    public static final String PRESENT_PROFIT_AUDIT_REJECT = "3";
    //市场方审批中
    public static final String PRESENT_PROFIT_AUDIT_PROCESSING = "4";
    //市场方审批成功
    public static final String PRESENT_PROFIT_AUDIT_APPROVED = "5";
    //银行处理
    public static final String PRESENT_PROFIT_BANK_PROCESSING = "6";
    //到账成功
    public static final String PRESENT_PROFIT_BANK_SUCCESS = "7";
    //到账失败
    public static final String PRESENT_PROFIT_BANK_FAIL = "8";

    // 提现：状态
    // 提现申请
    public static final String PRESENT_STATUS_APPLY = "1";
    // 市场方审批中
    public static final String PRESENT_STATUS_AUDIT = "2";
    // 提现申请
    public static final String PRESENT_STATUS_BANK_PROCESSING = "3";
    // 提现申请
    public static final String PRESENT_STATUS_BANK_SUCCESS = "4";

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
    public static final String PRESENT_STATUS_CASH_APPLY = "发起提现申请";
    // 保证金提现状态-银行处理中
    public static final String PRESENT_STATUS_CASH_PROCESSING = "银行处理中";
    // 保证金提现状态-到账成功
    public static final String PRESENT_STATUS_CASH_SUCCESS = "到账成功";

    // 保证金提现状态-保证金提现中
    public static final String PRESENT_CASH_PROCESSING = "保证金提现中";
    // 保证金提现状态-保证金提现
    public static final String PRESENT_CASH_SUCCESS = "保证金提现";
}
