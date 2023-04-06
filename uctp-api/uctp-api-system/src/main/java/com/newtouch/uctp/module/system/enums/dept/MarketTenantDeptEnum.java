package com.newtouch.uctp.module.system.enums.dept;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author helong
 * @date 2023/4/6 18:46
 */
@Getter
@AllArgsConstructor
public enum MarketTenantDeptEnum {
    MARKET(1, "市场方"),

    MERCHANT(2, "商户方");

    /**
     * 类型
     */
    private Integer type;
    /**
     * 描述
     */
    private String desc;
}
