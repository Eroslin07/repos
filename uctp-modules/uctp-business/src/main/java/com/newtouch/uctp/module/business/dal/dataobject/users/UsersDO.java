package com.newtouch.uctp.module.business.dal.dataobject.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import liquibase.pro.packaged.S;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 车辆主表 DO
 *
 * @author hr
 */
@TableName("system_users")
//@KeySequence("uctp_car_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDO extends TenantBaseDO {
    /**
     * id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 备注
     */
    private String remark;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    private String postIds;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 帐号状态（0正常 1停用 2未激活）
     */
    private Integer status;
    /**
     * 最后登陆IP
     */
    private String loginIp;
    /**
     * 最后登陆日期
     */
    private Date login_date;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    private String updater;
    /**
     * 更新日期
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    private Boolean deleted;



}
