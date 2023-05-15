package com.newtouch.uctp.module.business.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资金管理枚举
 */
public enum AccountEnum {
    // 保证金：交易类型
    TRADE_TYPE_RECHARGE("10011001", "保证金充值"),
    TRADE_TYPE_BACK("10011002", "保证金回填"),
    TRADE_TYPE_PROFIT_BACK("10011003", "保证金回填(利润)"),
    TRADE_TYPE_WITHDRAW("10011004", "保证金提现"),
    TRADE_TYPE_WITHDRAWING("10011005", "保证金提现中"),
    TRADE_TYPE_DEDUCTION("10011006", "保证金实扣"),
    TRADE_TYPE_PREEMPTION("10011007", "保证金预扣"),
    TRADE_TYPE_RELEASE("10011008", "保证金预扣释放"),

    //损益类型
    PROFIT_LOSS_TYPE_INCOME("10021001", "收入"),
    PROFIT_LOSS_TYPE_DISBURSEMENT("10021002", "支出"),
    PROFIT_LOSS_TYPE_IGNORE("10021003", "不计入"),

    //支付渠道
    PAY_CHANNEL_BANK("10031001", "浦发银行"),
    PAY_CHANNEL_PLATFORM("10031002", "平台"),

    //交易去向
    TRADE_TO_BUY_CARS("10041001", "收车"),
    TRADE_TO_MY_CASH("10041002", "我的保证金"),
    TRADE_TO_MARKET("10041003", "市场方"),
    TRADE_TO_MY_PROFIT("10041004", "我的利润"),

    //商户类型
    CUST_TYPE_MERCHANT("10051001", "商户"),
    CUST_TYPE_BUYER("10051002", "买车"),
    CUST_TYPE_SELLER("10051003", "卖车"),

    //交易状态
    TRAN_STATE_WAIT("10061001", "待支付"),
    TRAN_STATE_DURING("10061002", "支付中"),
    TRAN_STATE_SUCCESS("10061003", "支付成功"),
    TRAN_STATE_FAIL("10061004", "支付失败"),

    //提现状态：利润
    PRESENT_STATUS_APPLY("10071001", "提现申请"),
    PRESENT_STATUS_AUDIT("10071002", "市场方审批中"),
    PRESENT_STATUS_BANK_PROCESSING("10071003", "银行处理中"),
    PRESENT_STATUS_BANK_SUCCESS("10071004", "提现成功"),

    //提现状态：保证金
    PRESENT_STATUS_CASH_APPLY("10081001", "发起提现申请"),
    PRESENT_STATUS_CASH_PROCESSING("10081002", "银行处理中"),
    PRESENT_STATUS_CASH_SUCCESS("10081003", "到账成功"),

    //保证金提现状态
    PRESENT_CASH_PROCESSING("10091001", "保证金提现中"),
    PRESENT_CASH_SUCCESS("10091002", "保证金提现"),

    // 利润：交易类型
    TRAN_PROFIT_SALES_INCOME("10101001", "卖车款"), // 卖车款
    TRAN_PROFIT_PRESENT("10101002", "利润提现"),
    TRAN_PROFIT_CASH_BACK("10101003", "利润-扣减"), // 本次利润扣减本次回填保证金
    TRAN_PROFIT_SERVICE_COST("10101004", "利润-服务费"),
    TRAN_PROFIT_TAX_COST("10101005", "利润-税费"),
    TRAN_PROFIT_SALES_PROFIT("10101006", "利润"),
    TRAN_PROFIT_CASH_DEDUCTION("10101007", "利润-扣减原待回填保证金"),// 本次利润扣减原待回填保证金
    TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT("10101008", "原有利润-扣减"), // 原有利润扣减本次回填保证金（或原待回填保证金）

    // 利润：提现状态
    PRESENT_PROFIT_CASH_BACK_WAIT("10111001", "待回填保证金"),
    PRESENT_PROFIT_CASH_BACK_DONE("10111002", "已回填保证金"),
    PRESENT_PROFIT_APPLY("10111000", "申请登记"),
    PRESENT_PROFIT_AUDIT_REJECT("10111003", "市场方退回"),
    PRESENT_PROFIT_AUDIT_PROCESSING("10111004", "市场方审批中"),
    PRESENT_PROFIT_AUDIT_APPROVED("10111005", "市场方审批成功"),
    PRESENT_PROFIT_BANK_PROCESSING("10111006", "银行处理"),
    PRESENT_PROFIT_BANK_SUCCESS("10111007", "到账成功"),
    PRESENT_PROFIT_BANK_FAIL("10111008", "到账失败"),

    // 待回填保证金：明细类型
    CASH_BACK_TYPE_WAIT("10121001", "待回填保证金"),
    CASH_BACK_TYPE_PROFIT_DEDUCTION("10121002", "利润抵扣额度"),

    //银行返回码
    TRAN_TYPE_RECHARGE_CASH("10131001", "保证金-充值"),
    TRAN_TYPE_PRESENT_CASH("10131002", "提现-保证金"),
    TRAN_TYPE_PRESENT_PROFIT("10131003", "提现-利润"),
    TRAN_TYPE_SELL_CAR("10131004", "商户卖车款"),
    TRAN_TYPE_BY_CAR("10131005", "商户收车款"),

    BUSINESS_TYPE_CASH("1", "个人-保证金"),
    BUSINESS_TYPE_PROFIT("2", "对公-利润"),

    //银行返回码
    BANK_RESULT_CODE_SUCCESS("0000", "交易成功"),
    BANK_RESULT_CODE_PROCESSING("1000", "交易处理中"),
    BANK_RESULT_CODE_UNKNOWN("0001", "未知错误"),
    BANK_RESULT_CODE_PARAM_CHECK_FAIL("0002", "参数校验失败"),
    BANK_RESULT_CODE_FAIL("0003", "异步交易失败"),

    // 商户银卡信息状态标记
    BANK_CARD_ENABLE("0", "银行卡信息启用"),
    BANK_CARD_DISABLE("1", "银行卡信息禁用");


    private String key;
    private String value;

    AccountEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Map<String, String> accountEnumMap;
    public static Map<String, String> bankResultCodeMap;


    static {
        accountEnumMap = Arrays.stream(AccountEnum.values())
                .collect(Collectors.toMap(AccountEnum::getKey, AccountEnum::getValue, (o1, o2) -> o1));

        //
        bankResultCodeMap = new HashMap<>();
        bankResultCodeMap.put(BANK_RESULT_CODE_SUCCESS.key, TRAN_STATE_SUCCESS.key);
        bankResultCodeMap.put(BANK_RESULT_CODE_PROCESSING.key, TRAN_STATE_DURING.key);
        bankResultCodeMap.put(BANK_RESULT_CODE_UNKNOWN.key, TRAN_STATE_FAIL.key);
        bankResultCodeMap.put(BANK_RESULT_CODE_PARAM_CHECK_FAIL.key, TRAN_STATE_FAIL.key);
        bankResultCodeMap.put(BANK_RESULT_CODE_FAIL.key, TRAN_STATE_FAIL.key);

    }

    public static String getName(String key) {
        if (key == null || key.trim().equals("")) {
            return null;
        }

        AccountEnum[] values = AccountEnum.values();
        for (AccountEnum value : values) {
            if (Objects.equals(value.key, key)) {
                return value.value;
            }
        }

        return null;
    }
}
