package com.newtouch.uctp.module.bpm.dal.dataobject.car;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

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
     * 合同状态
     */
    private Integer status;
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

//    /**
//     * 创建人
//     */
//    private String creator;
//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
//    /**
//     * 更新人
//     */
//    private String updater;
//    /**
//     * 更新时间
//     */
//    private LocalDateTime updateTime;
//

}
