package com.newtouch.uctp.module.business.mq.message;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户认证消息
 *
 * @author lc
 */
@Data
public class UserAuthMessage {

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 认证电话
     */
    @NotNull(message = "认证电话不能为空")
    private String contract;

    @NotNull(message = "发送次数")
    private Integer count = 1;

}
