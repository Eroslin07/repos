package com.newtouch.uctp.module.bpm.dal.dataobject.form;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import com.newtouch.uctp.module.bpm.annotation.WfEntity;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;

/**
 * 流程业务主表（通用）
 * @author helong
 * @date 2023/4/8 15:31
 */
@TableName(value = "bpm_form_main", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@WfEntity(alias = "formMain", isMainEntity = true, mapperClass = BpmFormMainMapper.class)
public class BpmFormMainDO extends BaseDO {
    /**
     * 业务ID
     */
    @TableId
    private Long id;
    /**
     * 单据状态（0-草稿  1-审批中   2-完成  -1-作废）
     */
    private Integer status;
    /**
     * 流程定义ID
     */
    private String procDefId;
    /**
     * 流程实例ID
     */
    private String procInstId;
    /**
     * 申请单号
     */
    private String serialNo;
    /**
     * 标题（事项）
     */
    private String title;
    /**
     * 申请人
     */
    private Long startUserId;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 业务类型（标识）
     */
    private String busiType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 流程创建提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 流程最后一次审批时间
     */
    private LocalDateTime doneTime;
    /**
     * 表单提交JSON二进制数据
     */
    private byte[] formDataJson;
    /**
     * 第三方业务ID
     */
    private Long thirdId;
}
