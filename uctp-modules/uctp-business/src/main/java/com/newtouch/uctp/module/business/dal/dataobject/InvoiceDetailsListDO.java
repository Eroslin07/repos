package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("uctp_invoices_details")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsListDO implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 增值税发票ID
     */
    private Long vatInvoiceID;
    /**
     * 明细行号
     */
    private String goodsLineNo;
    /**
     * 发票行性质，0：正常行 1：折扣行 2：被折扣行
     */
    private String goodsLineNature;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
     * 自行编码
     */
    private String goodsExtendCode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品税目
     */
    private String goodsTaxItem;
    /**
     * 规格型号
     */
    private String goodsSpecification;
    /**
     * 计量单位
     */
    private String goodsUnit;
    /**
     * 商品数量
     */
    private String goodsQuantity;
    /**
     * 商品单价
     */
    private String goodsPrice;
    /**
     * 金额
     */
    private String goodsTotalPrice;
    /**
     * 税额，如果为空，根据金额，税率自动计算出
     */
    private String goodsTotalTax;
    /**
     * 税率
     */
    private String goodsTaxRate;
    /**
     * 折行对应行号（有折扣行时必填）
     */
    private String goodsDiscountLineNo;
    /**
     * 含税标志0：不含税 1：含税
     */
    private String priceTaxMark;

    /**
     * 增优惠政策类型，如果使用优惠政策，此项必填，具体信息取《商品
     * 和服务税收分类与编码》.xls中的增值税特殊管理列。
     */
    private String vatSpecialManagement;
    /**
     * 零税率标识：空代表无， 1 出口免税和其他免税优惠政策， 2 不征增值
     * 税， 3 普通零税率
     */
    private String freeTaxMark;
    /**
     * 是否使用优惠政策 0:未使用，1:使用
     */
    private String preferentialMark;

}
