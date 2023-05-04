package com.newtouch.uctp.module.business.controller.app.qys.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 合同状态回调处理的DTO
 */
@Data
@ToString(callSuper = true)
public class DoServiceDTO {
    /**
     * 合同状态
     */
    private Integer contratStatus;
    /**
     * 第一级状态
     */
    private Integer salesStatus;
    /**
     * 第二级状态
     */
    private Integer status;
    /**
     * 第三级状态
     */
    private Integer statusThree;
    /**
     * 是否发送
     */
    private Boolean send = Boolean.FALSE;
    /**
     * 是否调用支付接口
     */
    private Boolean pay = Boolean.FALSE;
}
