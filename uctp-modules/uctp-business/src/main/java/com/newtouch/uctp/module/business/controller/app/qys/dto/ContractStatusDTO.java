package com.newtouch.uctp.module.business.controller.app.qys.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 合同状态返回值
 */
@Data
@ToString(callSuper = true)
public class ContractStatusDTO {
    /**
     * 当前回调的合同Id
     */
    private Long contractId;
    /**
     * 当前回调的通知类型，具体类型信息见后方列表
     */
    private String callbackType;
    /**
     * 当前回调的合同状态，具体类型信息见后方列表
     */
    private String contractStatus;
    /**
     * 签署方类型: PERSONAL-个人 COMPANY-公司
     */
    private String tenantType;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 签署方ID
     */
    private Long tenantId;

}
