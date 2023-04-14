package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
public class TaxRateDTO {
    private String type; // 税率类型
    private BigDecimal reate; // 税率
}
