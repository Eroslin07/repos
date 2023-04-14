package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("uctp_car_transaction_invoices")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarTransactionInvoiceDataDO implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 二手车发票关联ID
     */
    private Long OutputInvoiceCarID;
    /**
     * 购方税号
     */
    private String buyerTaxNo;
    /**
     * 购方名称
     */
    private String buyerName;
    /**
     * 购方地址
     */
    private String buyerAddress;
    /**
     * 购方电话
     */
    private String buyerPhone;
    /**
     * 开票流水号 唯一标志开票请求
     */
    private String serialNo;
    /**
     * 开票人
     */
    private String drawer;
    /**
     * 开票类型 0:正数发票（蓝票） 1：负数发票（红票）
     */
    private String invoiceType;
    /**
     * 发票种类编码： 006 二手车
     */
    private String invoiceTypeCode;
    /**
     * 原发票代码（负数发票必填）
     */
    private String originalInvoiceCode;
    /**
     * 原发票号码（负数发票必填）
     */
    private String originalInvoiceNo;
    /**
     * 合计金额，保留两位小数
     */
    private BigDecimal invoiceTotalPrice;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 用户账号，用于个人维度区分
     */
    private String userAccount;
    /**
     * 二手车反向开具标识：0-非反向开具、1-反向开具，为空=非反向开具
     */
    private String reverseIssueMark;

    /**
     * 二手车明细
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private InvoiceDetailsListDO invoiceVehicleInfoDO;
}
