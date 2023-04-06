package com.newtouch.uctp.module.system.enums.tenant;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 租户套餐类型
 * @author helong
 * @date 2023/4/4 17:51
 */
@Getter
@AllArgsConstructor
public enum TenantPackageTypeEnum {

    MARKET(1, "市场型"),

    MERCHANT(2, "商户型");

    /**
     * 类型
     */
    private Integer type;
    /**
     * 描述
     */
    private String desc;

    public static TenantPackageTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
