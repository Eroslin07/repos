package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同主表 DO
 *
 * @author qjj
 */
@TableName("uctp_contract")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDO extends TenantBaseDO {
    /**
     * 合同主表id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 车辆id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carId;

    /**
     * 契约锁合同id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 商户id
     */
    private Long businessId;
}