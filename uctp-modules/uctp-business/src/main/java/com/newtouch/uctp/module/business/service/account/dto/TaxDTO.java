package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class TaxDTO {
    private String type; // 税率类型（中文名称）
    private BigDecimal rate; // 税率，传小数（如：5%传0.05）
    private Long amount; // 税费（单位为：分），不需要传入，资金处理自动计算
}
