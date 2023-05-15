package com.newtouch.uctp.module.business.service.bank.dto;

import lombok.Data;

/**
 * 余额出金
 */
@Data
public class OutGoldDTO {

    /**
     * 交易类型
     */
    private String tranType;

    /**
     * 交易合同号
     */
    private Long contractNo;

    /**
     * 平台商户虚拟账户号
     */
    private String accountNo;

    /**
     * 子账户开户行名称
     */
    private String openBankName;

    /**
     * 子账户开户行号
     */
    private String openBranchNo;

    /**
     * 收款方户名
     */
    private String payeeAcctName;

    /**
     * 收款方账号
     */
    private String payeeAcctNo;

    /**
     * 交易金额
     */
    private String tranAmt;

    /**
     * 支付去向
     */
    private String to;

    public enum PayTo {
        INTERNAL("1", "行内提现出金"),
        OUT("2", "收车款出金");

        private String code;

        private String desc;

        PayTo(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
