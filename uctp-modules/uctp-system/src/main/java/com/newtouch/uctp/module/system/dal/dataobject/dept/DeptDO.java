package com.newtouch.uctp.module.system.dal.dataobject.dept;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.enums.dept.MarketTenantDeptEnum;

/**
 * 部门表
 *
 * @author ruoyi
 * @author 芋道源码
 */
@TableName("system_dept")
@KeySequence("system_dept_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends TenantBaseDO {

    /**
     * 部门ID
     */
    @TableId
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门ID
     *
     * 关联 {@link #id}
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 负责人
     *
     * 关联 {@link AdminUserDO#getId()}
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 部门属性
     *
     * 枚举 {@link MarketTenantDeptEnum}
     */
    private Integer attr;

    /**
     * 营业执照号
     */
    private String taxNum;
    /**
     * 营业执照上传url
     */
    private String businessLicenseUrl;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 联系地址
     */
    private String address;
    /**
     * 是否经过契约锁认证(0-认证中，1-认证成功，2-认证失败,3-未认证)
     */
    private Integer auth;
}
