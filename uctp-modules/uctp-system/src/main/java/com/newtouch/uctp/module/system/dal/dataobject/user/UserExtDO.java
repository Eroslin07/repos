package com.newtouch.uctp.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import com.newtouch.uctp.module.system.enums.common.SexEnum;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户扩展表 DO
 *
 * @author qjj
 */
@TableName(value = "uctp_user_ext", autoResultMap = true)
@KeySequence("uctp_user_ext_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExtDO extends TenantBaseDO {

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户主表id
     */
    private Long userId;

    /**
     * 对公银行账号
     */
    private String bankAccount;


    /**
     * 开户行
     */
    private String bankName;
    /**
     * 商户ID
     */
    private Long businessId;

    /**
     *用户类型
     */
    private String staffType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 保证金充值卡号
     */
    private String bondBankAccount;

    /**
     * 契约锁认证id
     */
    private Integer authId;
}
