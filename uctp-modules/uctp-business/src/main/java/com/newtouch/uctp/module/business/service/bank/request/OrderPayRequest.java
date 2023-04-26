package com.newtouch.uctp.module.business.service.bank.request;

import lombok.Data;

/**
 * 订单支付请求报文
 */
@Data
public class OrderPayRequest {

    /**
     * 商户编号
     */
    private String mrchNo;

    /**
     * 支付方式
     */
    private String pymtMd;

    /**
     * 门店编号
     */
    private String strNo;

    /**
     * 门店描述
     */
    private String pdPrcDsc;

    /**
     * 收银员编号
     */
    private String cshrNo;

    /**
     * 学号
     */
    private String stdntId;

    /**
     * 学生姓名
     */
    private String pcsName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 教室明细
     */
    private String clssrmDtl;

    /**
     * 课程明细
     */
    private String crrclumDtl;

    /**
     * 班级明细
     */
    private String clssDtl;

    /**
     * 开课时间
     */
    private String startClssTime;

    /**
     * 费用明细
     */
    private String feeCodeDsc;

    /**
     * 订单金额
     */
    private String ordrAmt;

    /**
     * 优惠金额
     */
    private String mdsctAmnt;

    /**
     * 实际支付金额
     */
    private String actPyAmt;

    /**
     * 商户订单号
     */
    private String mrchOrdrNo;

    /**
     * 订单创建日期
     */
    private String rsrvFld1;
}
