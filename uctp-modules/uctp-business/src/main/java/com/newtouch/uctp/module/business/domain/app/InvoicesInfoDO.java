package com.newtouch.uctp.module.business.domain.app;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * 车辆主表 DO
 *
 *
 */
@TableName("uctp_invoices_management")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicesInfoDO extends BaseDO {



    /**
     * ID
     */
    @TableId
    private String id;
    /**
     * 发票类型(0反向二手车销售统一发票 1正向二手车销售统一发票 2二手车增值税发票)
     */
    private String invoiceType;
    /**
     * 商户
     */
    private String business;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同金额
     */
    private String amountMoney;
    /**
     * 税额
     */
    private String tax;
    /**
     * 状态(0待开票 1已开票)
     */
    private String status;
    /**
     * 租户号
     */
    private String tenantId;
    /**
     * 创建人
     */

    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private Long updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 车辆ID
     */
    private String carId;


}
