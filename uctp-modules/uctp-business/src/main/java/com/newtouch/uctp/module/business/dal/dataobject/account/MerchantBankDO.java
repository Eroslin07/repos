package com.newtouch.uctp.module.business.dal.dataobject.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

@TableName("uctp_merchant_bank")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantBankDO extends BaseDO {

    @TableId
    private Long id;//账户主键标识
    private String accountNo; // 商户账户号
    private String businessType; //业务类型',
    private String bankName; //开户银行名称',
    private String bankNo;//银行账号',
    private String childAcctNo;//结算银行子账号
    private Integer revision;//乐观锁

}
