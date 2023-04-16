package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CostDTO {
    private String type; // 费用类型
    private Integer amount; // 费用金额（单位为：分）
    private boolean promptPayment; // 立即付款
    private String bankNo; // 费用收款银行卡号（立即付款时填写）
    private String bankName;// 费用收款银行名称（立即付款时填写）
}
