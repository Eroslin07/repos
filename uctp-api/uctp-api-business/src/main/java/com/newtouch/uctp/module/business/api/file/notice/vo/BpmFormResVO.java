package com.newtouch.uctp.module.business.api.file.notice.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author hr
 * @date 2023/4/17 19:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BpmFormResVO {
    /**
     * 业务ID
     */
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
    private JSONObject formDataJson;
    /**
     * 第三方业务ID
     */
    private Long thirdId;
}
