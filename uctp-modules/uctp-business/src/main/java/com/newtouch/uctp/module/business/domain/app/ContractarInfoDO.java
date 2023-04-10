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
@TableName("uctp_contract_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractarInfoDO extends BaseDO {



    /**
     * ID
     */
    @TableId
    private String id;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 商户ID
     */
    private String businessId;
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
