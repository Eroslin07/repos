package com.newtouch.uctp.framework.qiyuesuo.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信渠道枚举
 *
 * @author zzf
 * @since 2021/1/25 10:56
 */
@Getter
@AllArgsConstructor
public enum QiyuesuoChannelEnum {
    //此模式需要拼接上
    DEFAULT("default", "默认契约锁模式"),
    SAAS("saas", "SAAS契约锁模式");

    /**
     * 编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static QiyuesuoChannelEnum getByCode(String code) {
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code), values());
    }

}
