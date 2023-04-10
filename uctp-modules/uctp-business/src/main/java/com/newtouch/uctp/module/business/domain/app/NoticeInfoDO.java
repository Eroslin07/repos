package com.newtouch.uctp.module.business.domain.app;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * 车辆主表 DO
 *
 * @author lc
 */
@TableName("uctp_notice")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeInfoDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型(0站内消息 1短信买家 2短信卖家)
     */
    private String type;
    /**
     * 接收人手机号
     */
    private String phone;
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
    private Integer createdBy;
    /**
     * 创建时间
     */
    private Long createdTime;

    /**
     * 更新人
     */
    private Integer updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;



}
