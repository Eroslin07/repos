package com.newtouch.uctp.module.business.dal.dataobject.qys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 契约锁 DO
 *
 * @author 芋道源码
 */
@TableName("uctp_qys_config")
//@KeySequence("uctp_qys_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QysConfigDO extends TenantBaseDO {

    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 访问地址
     */
    private String serverUrl;
    /**
     * 访问秘钥
     */
    private String accessKey;
    /**
     * 访问密匙
     */
    private String accessSecret;
    /**
     * saas秘钥
     */
    private String secret;
    /**
     * 编码
     */
    private String code;
    /**
     * 状态(0未启用 1启用)
     */
    private Integer status;
    /**
     * 商户名称
     */
    private String businessName;
    /**
     * 商户Id
     */
    private Long businessId;
    /**
     * 契约锁公司id
     */
    private Long companyId;

}
