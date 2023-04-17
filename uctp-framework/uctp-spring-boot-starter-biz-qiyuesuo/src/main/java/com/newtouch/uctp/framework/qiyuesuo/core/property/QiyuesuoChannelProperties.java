package com.newtouch.uctp.framework.qiyuesuo.core.property;

import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 契约锁渠道配置类
 *
 * @author lc
 * @since 2021/1/25 17:01
 */
@Data
@Validated
public class QiyuesuoChannelProperties {
    /**
     * 渠道编号
     */
    @NotNull(message = "契约锁渠道 ID 不能为空")
    private Long id;
    /**
     * 渠道编码
     *
     * 枚举 {@link QiyuesuoChannelEnum}
     */
    @NotEmpty(message = "渠道编码不能为空")
    private String code;
    /**
     * 契约锁 key
     */
    @NotEmpty(message = "契约锁key")
    private String apiKey;
    /**
     * 契约锁的密钥
     */
    @NotEmpty(message = "契约锁的密钥不能为空")
    private String apiSecret;
}
