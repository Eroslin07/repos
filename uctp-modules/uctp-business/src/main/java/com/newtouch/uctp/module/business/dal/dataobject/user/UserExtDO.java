package com.newtouch.uctp.module.business.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 用户扩展表 DO
 *
 * @author hr
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
     * 保证金开户行
     */
    private String bondBankName;
    /**
     * 保证金充值卡号
     */
    private String bondBankAccount;
    /**
     * 个人认证id
     */
    private String authId;
}
