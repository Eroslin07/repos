package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("uctp_output_invoice_used_car_issue")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputInvoiceUsedCarIssueDO implements Serializable {
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
     * 销方机构税号
     */
    private String taxNo;
    /**
     * 如果是非纳税主体，设置开票信息中的orgId，根据判断自行信息管理选择是否设置开票相关信息
     */
    private String orgCode;
    /**
     * 票终端代码 ,如果只有一个终端可不填，多个终端必填
     */
    private String invoiceTerminalCode;
    /**
     * 盘号终端选填，如果只有一个终端可不填
     */
    private String taxDiskNo;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private CarTransactionInvoiceDataDO carTransactionInvoiceDataDO;
}
