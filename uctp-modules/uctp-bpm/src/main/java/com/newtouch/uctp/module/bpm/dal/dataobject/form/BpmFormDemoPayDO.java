package com.newtouch.uctp.module.bpm.dal.dataobject.form;

import lombok.*;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import com.newtouch.uctp.module.bpm.annotation.WfEntity;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormDemoPayMapper;

/**
 * 流程表单支付记录Demo表（副表）
 * @author helong
 * @date 2023/4/10 15:15
 */
@TableName(value = "bpm_form_demo_pay", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@WfEntity(alias = "formDemoPay", mapperClass = BpmFormDemoPayMapper.class)
public class BpmFormDemoPayDO extends BaseDO {
    /**
     * 实体ID
     */
    @TableId
    private Long id;
    /**
     * 业务ID
     */
    private Long businessId;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 支付备注
     */
    private String remark;
}
