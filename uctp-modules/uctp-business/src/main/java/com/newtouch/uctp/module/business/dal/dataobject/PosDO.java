package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * POS机主表 DO
 *
 * @author qjj
 */
@TableName("uctp_pos")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosDO extends TenantBaseDO {
    /**
     * pos机主表id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * POS机名称
     */
    private String posName;

    /**
     * pos机编号
     */
    private String posId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(0停用  1启用)
     */
    private String status;

    /**
     * 商户id
     */
    private Long businessId;


}
