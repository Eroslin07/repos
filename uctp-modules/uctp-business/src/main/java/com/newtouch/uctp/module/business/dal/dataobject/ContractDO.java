package com.newtouch.uctp.module.business.dal.dataobject;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;

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
     * 契约锁合同文件id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long documentId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 甲方
     */
    private String firstParty;

    /**
     * 乙方
     */
    private String secondParty;

    /**
     * 签约金额
     */
    private String signUpAmount;

    /**
     * 合同状态(0 草稿 1已发起 2已完成)
     */
    private Integer status;
    /**
     * 是否作废 ；0未作废，1作废
     */
    private Integer invalided;
    /**
     * 合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
     */
    private Integer contractType;

    /**
     * 商户id
     */
    private Long businessId;

    /**
     * 签约时间
     */
    private LocalDateTime signingDate;
    /**
     * 合同编码
     */
    private String code;

}
