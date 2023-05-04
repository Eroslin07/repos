package com.newtouch.uctp.module.business.service.bank.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单支付请求报文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayRequest {

    /**
     * 商户编号-必填
     */
    private String mrchNo;

    /**
     * 支付方式-必填
     */
    private String pymtMd;

    /**
     * 门店编号-必填
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
     * 学号-必填
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
     * 订单金额-必填
     */
    private String ordrAmt;

    /**
     * 优惠金额
     */
    private String mdsctAmnt;

    /**
     * 实际支付金额-必填
     */
    private String actPyAmt;

    /**
     * 商户订单号-必填
     */
    private String mrchOrdrNo;

    /**
     * 订单创建日期
     */
    private String rsrvFld1;

    /**
     *
     * @param mrchNo        商户编号-必填
     * @param pymtMd        支付方式-必填
     * @param strNo         门店编号-必填
     * @param stdntId       学号-必填
     * @param ordrAmt       订单金额-必填
     * @param mrchOrdrNo    商户订单号-必填
     *
     *
     * @return  浦发银行支付接口请求参数必填参数对象
     */
    public static OrderPayRequest buildPayReq(String mrchNo, String pymtMd, String strNo, String stdntId, String ordrAmt, String mrchOrdrNo) {
        return OrderPayRequest.builder()
                .mrchNo(mrchNo)
                .pymtMd(pymtMd)
                .strNo(strNo)
                .stdntId(stdntId)
                .ordrAmt(ordrAmt)
                .actPyAmt(ordrAmt)  //实际支付金额-必填 = 订单金额-必填
                .mrchOrdrNo(mrchOrdrNo)
                .build();
    }
}
