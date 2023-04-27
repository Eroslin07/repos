package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
     * 类型(0站内消息 1短信推送消息)
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
    private Long tenantId;
    /**
     * 消息状态
     */
    private String status;
    /**
     * 跳转路径/有就存，没有就不存
     */
    private String url;
    /**
     * 消息推送状态,只展示未推送消息
     */
    private String pushStatus;
    /**
     * 消息推送失败原因
     */
    private String errorMsg;
    /**
     * 消息推送失败次数
     */
    private String errorNum;
   /* *//**
     * 内容模版枚举类型
     *//*
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String  contentType;

    *//**
     * 原因
     *//*
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String  reason;
    *//**
     * 合同号
     *//*
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String  contractNo;
    *//**
     * 合同名称
     *//*
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String  contractName;
    *//**
     * 金额（收车/卖车）
     *//*
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String price;*/

}
