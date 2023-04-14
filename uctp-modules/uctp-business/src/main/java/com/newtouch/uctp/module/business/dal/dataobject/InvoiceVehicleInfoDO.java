package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("uctp_invoices_vehicle")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceVehicleInfoDO implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 车牌交易关联ID
     */
    private Long carTranstionID;
    /**
     * 车牌照号
     */
    private String licensePlate;
    /**
     * 开票方类型：01-经营单位、02-拍卖单位、 03-二手车市场
     */
    private String drawerType;
    /**
     * 车辆类型
     */
    private String vehicleType;
    /**
     * 登记证号
     */
    private String registryNo;
    /**
     * 厂牌型号
     */
    private String brandModel;
    /**
     * 转入地车辆管理所名称
     */
    private String vehicleAdministration;
    /**
     * 车辆识别代号/车架号码，车架号码只能为字母、数字或*号
     */
    private String vehicleNo;
    /**
     * 车购税完税凭证号码
     */
    private String carPaymentVoucherNo;
    /**
     * 开票标识，0-未开票 1-已开票
     */
    private String invoiceMark;
    /**
     * 增值税标志，0-免征增值税 1-不征增值税
     */
    private String vatMark;
    /**
     * 已开发票代码
     */
    private String issuedInvoiceCode;
    /**
     * 已开发票号码
     */
    private String issuedInvoiceNo;
    /**
     * 已开票销售额
     */
    private BigDecimal issuedTotalPrice;
    /**
     * 已开票税额
     */
    private BigDecimal issuedTotalTax;
    /**
     * 已开票税率
     */
    private BigDecimal issuedTaxRate;

    /**
     * 开具完税证明标识，0-未开 1-已开
     */
    private String paymentVoucherMark;
    /**
     * 完税凭证号码
     */
    private String paymentVoucherNo;
    /**
     * 完税凭证销售额，保留两位小数
     */
    private BigDecimal paymentVoucherToralPrice;
    /**
     * 完税凭证税率，保留两位小数
     */
    private BigDecimal paymentVoucherTaxRate;
    /**
     * 完税凭证税额，保留两位小数
     */
    private BigDecimal paymentVoucherTotalTax;
    /**
     * 商品编码，总局固定编码，不能修改
     */
    private String goodsCode;
    /**
     * 自行编码，以2位为一级，共10级，每级可用编码值为00-99或AA-ZZ
     */
    private String goodsPersonalCode;
    /**
     * 增值税特殊管理（优惠政策为1时必填）
     */
    private String vatSpecialManagement;
    /**
     * 零税率标识 ，空代表无， 1-出口免税和其他免税优惠政策 2-不征增值税 3-普通零税率
     */
    private String freeTaxMark;
    /**
     * 优惠政策标识，是否使用优惠政策 0-未使用 1-使用
     */
    private String preferentialMarkFlag;
    /**
     * 卖方单位名称，支持汉字，数字和字母，开票方类型为二手车市场必填
     */
    private String sellerName;
    /**
     * 卖方单位代码，只支持数字和字母，开票方类型为二手车市场必填
     */
    private String sellerTaxNo;
    /**
     * 卖方单位地址
     */
    private String sellerAddress;
    /**
     * 卖方单位电话
     */
    private String sellerPhone;


}
