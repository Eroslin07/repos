package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;

@Data
public class CollectPaymentFailedFormDTO {

    /**
     * 组户ID
     */
    private Long tenantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 收车合同号
     */
    private String collectContractNo;

    /**
     * 收车委托合同
     */
    private String commissionContractNo;

    /**
     * 商户代办人
     */
    private String responsible;

    /**
     * 商户电话
     */
    private String mobile;

    /**
     * 卖车方姓名
     */
    private String sellName;

    /**
     * 收款账号
     */
    private String sellPayeeAccountNo;

    /**
     * 收车款
     */
    private String amount;

    /**
     * 支付失败原因
     */
    private String reason;

    /**
     * 付款人名称
     */
    private String payName;

    /**
     * 支付方开户行名称
     */
    private String payOpenBranchName;

    /**
     * 支付方银行账号
     */
    private String payAccountNo;
}
