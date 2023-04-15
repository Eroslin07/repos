package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class CostDTO {
    private String type; // 费用类型
    private Integer amount; // 费用金额（单位为：分）
}
