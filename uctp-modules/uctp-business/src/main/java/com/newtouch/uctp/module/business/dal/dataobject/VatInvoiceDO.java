package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("uctp_vat_invoices")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VatInvoiceDO implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 设备类型 0税控服务器，1税控盘，3凯盈盒子， 5税务ukey
     */
    private String deviceType;
    /**
     * 组织机构代码，不为空表示所开票归属于当前机构
     */
    private String organizationCode;
    /**
     * 开票流水号，唯一标志开票请求。支持数字字母下划线组合。
     */
    private String serialNo;
    /**
     * 业务请求流水号
     */
    private String reqSerialNumber;
    /**
     * 第三方业务系统ID
     */
    private String businessId;
    /**
     * 系统ID
     */
    private String systemId;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 征税方式 0：普通征税，2：差额征税
     */
    private String taxationMode;
    /**
     * 开票类型0:正数发票（蓝票） 1：负数发票（红票）
     */
    private String invoiceType;
    /**
     * 发票种类编码，004:增值税专用发票，007:增值税普通发票，026：增值税电子发票，025：增值税卷式发票，028：增值税电子专用发票
     */
    private String invoiceTypeCode;
    /**
     * 特殊票种标记，00：普通发票 02：农业发票 06：抵扣通行费 07：其他通行费 08：成品油  默认：00
     */
    private String invoiceSpecialMark;
    /**
     * 销方单位税号
     */
    private String sellerTaxNo;
    /**
     * 开票点编码
     */
    private String invoiceTerminalCode;
    /**
     * 购方单位税号
     */
    private String buyerTaxNo;

    /**
     * 购方单位名称
     */
    private String buyerName;
    /**
     * 购方地址及电话，专（含专电）票必填
     */
    private String buyerAddressPhone;
    /**
     * 购方开户行及账号，专（含专电）票必填
     */
    private String buyerBankAccount;
    /**
     * 开票人,电子发票8个字符；专（含电专）普票16个字符
     */
    private String drawer;
    /**
     * 复核人，电子发票8个字符；专（含电专）普票16个字符
     */
    private String checker;
    /**
     * 收款人，电子发票8个字符；专（含电专）普票16个字符
     */
    private String payee;
    /**
     * 清单标志： 0：无清单 1：带清单 （发票明细大于等于8行必须带清单）
     */
    private String invoiceListMark;

    /**
     * 红字信息表编号，开具红字信息表
     */
    private String redInfoNo;
    /**
     * 通知单编号
     */
    private String notificationNo;
    /**
     * 原发票代码(开红票时传入)
     */
    private String originalInvoiceCode;
    /**
     * 原发票号码(开红票时传入)
     */
    private String originalInvoiceNo;
    /**
     * 扣除额，当征税方式为差额征税时必填。数值必须小于价税合计。注：税控服务器差额征税开负票时，扣除额必须为空。
     */
    private BigDecimal deductibleAmount;
    /**
     * 合计金额，保留两位小数
     */
    private BigDecimal invoiceTotalPrice;
    /**
     * 合计税额，保留两位小数
     */
    private BigDecimal invoiceTotalTax;

    /**
     * 价税合计，保留两位小数
     */
    private BigDecimal invoiceTotalPriceTax;
    /**
     * 签名值参数; 默认为：0000004282000000
     */
    private String signatureParameter;
    /**
     * 税控盘编号，设备类型为1时必填
     */
    private String taxDiskNo;
    /**
     * 商品编码版本号
     */
    private String goodsCodeVersion;
    /**
     * 综合税率
     */
    private String consolidatedTaxRate;
    /**
     * 发票票样，01：76mm*177mm 最多6行 02：76mm*152mm 最多3行 03：57mm*177mm 最多6行 04：57mm*152mm 最多3行 05：湖南票样 06：76mm*177.8mm(全国通用) 07：57mm*177.8mm(全国通用) 特殊说明： 票样06 每行最多打印14个字节 1） 备注为空 ：票面可以打印总行数为22行；2）备注不为空 ：票面可以打印总行数为20行；票样07 每行最多打印10个字节 1） 备注为空 ：票面可以打印总行数为19行；2）备注不为空 ：票面可以打印总行数为16行； 开具商品总条数由各条商品所占行数（通过项目名称长度、每行打印字节数、有无备注计算单挑商品所占行数）累加后不超过打印总行数来计算。）
     */
    private String invoiceSample;

    /**
     * 开具超过限额自动拆分, true：自动拆分，false：不拆分，默认不拆分。
     */
    private Boolean autoSplit;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 销方
     */
    private String sellerName;
    /**
     * 销方银行账号
     */
    private String sellerBankAccount;
    /**
     * 销方地址电话
     */
    private String sellerAddressPhone;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 商品明细实体
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private InvoiceDetailsListDO invoiceDetailsListDO;

}



