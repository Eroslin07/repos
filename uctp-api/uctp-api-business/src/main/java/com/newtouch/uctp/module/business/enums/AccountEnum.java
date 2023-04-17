package com.newtouch.uctp.module.business.enums;

/**
 * 资金管理枚举
 */
public enum AccountEnum {
    //损益类型：
    PROFIT_LOSS_TYPE_INCOME("1", "收入"),
    PROFIT_LOSS_TYPE_DISBURSEMENT("2", "支出"),

    //交易去向：
    TRADE_TO_BUY_CARS("1", "收车"),
    TRADE_TO_MY_CASH("2", "我的保证金"),
    TRADE_TO_MARKET("3", "市场方"),
    TRADE_TO_MY_PROFIT("4", "我的利润"),

    // 利润：交易类型
    TRAN_PROFIT_SALES_PROFIT("1", "卖车利润"),
    TRAN_PROFIT_PRESENT("2", "利润提现"),
    TRAN_PROFIT_CASH_BACK("3", "利润-保证金回填"),
    TRAN_PROFIT_SERVICE_COST("4", "利润-服务费"),
    TRAN_PROFIT_TAX_COST("5", "利润-税费"),

    // 利润：提现状态
    PRESENT_PROFIT_CASH_BACK_WAIT("1", "待回填保证金"),
    PRESENT_PROFIT_CASH_BACK_DONE("2", "已回填保证金"),
    PRESENT_PROFIT_APPLY("0", "申请登记"),
    PRESENT_PROFIT_AUDIT_REJECT("3", "市场方退回"),
    PRESENT_PROFIT_AUDIT_PROCESSING("4", "市场方审批中"),
    PRESENT_PROFIT_AUDIT_APPROVED("5", "市场方审批成功"),
    PRESENT_PROFIT_BANK_PROCESSING("6", "银行处理"),
    PRESENT_PROFIT_BANK_SUCCESS("7", "到账成功"),
    PRESENT_PROFIT_BANK_FAIL("8", "到账失败");

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
}
