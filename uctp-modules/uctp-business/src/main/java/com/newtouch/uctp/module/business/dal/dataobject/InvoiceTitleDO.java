package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.util.Date;

/**
 * 发票抬头配置 DO
 *
 *
 */
@TableName("uctp_invoice_title_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceTitleDO extends TenantBaseDO {




    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     *纳税人识别号
     */
    private String taxNum;

    /**
     *电话
     */
    private String tel;

    /**
     *地址
     */
    private String address;

    /**
     *开户银行
     */
    private String bank;

    /**
     *银行账号
     */
    private String bankAccount;

    /**
     *  公司名称
     */
    private String companyName;

    /**
     *法定代表人
     */
    private String represent;



    /**
     *开户行及账号
     */
    private String opening;



    /**
     *部门Id
     */
    private Long deptId;













}
