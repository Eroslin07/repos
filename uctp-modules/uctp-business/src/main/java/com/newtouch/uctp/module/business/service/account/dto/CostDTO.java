package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
public class CostDTO {
    private String type; // 费用类型
    private BigDecimal amount; // 费用金额
}
