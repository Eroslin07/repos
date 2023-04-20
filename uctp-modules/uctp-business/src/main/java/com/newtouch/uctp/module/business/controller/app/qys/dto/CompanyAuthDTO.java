package com.newtouch.uctp.module.business.controller.app.qys.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 企业认证返回值
 */
@Data
@ToString(callSuper = true)
public class CompanyAuthDTO {
    /**
     * 企业ID
     */
    private String companyId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 认证结果状态：0（“认证中”）、1（“认证成功”）、2（“认证失败”）
     */
    private String status;
    /**
     * 认证行为状态：0（“提交基本信息”）、1（“基本信息审核通过”）、2（“基础信息审核失败”）、4（“授权书审核失败”）、7（“认证成功”）
     */
    private String actionEvent;
    /**
     * 认证请求Id
     */
    private String requestId;
    /**
     * 认证企业的工商注册号
     */
    private String registerNo;
    /**
     * 认证企业的法人代表姓名
     */
    private String legalPerson;

}
