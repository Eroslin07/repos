package com.newtouch.uctp.module.business.dal.dataobject.cash;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

@TableName("uctp_merchant_cash")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCashDO extends BaseDO {

    @TableId
    private Long id;//主键标识

    private Long accountId;//账户标识

    private String accountNo;//商户账户号

    private String payChannel;//支付渠道

    private String profitLossType;//损益类型

    private String tranRecordNo;//支付流水

    private Long payAmount;//支付金额

    private String tradeType;//交易类型

    private String tradeTo;//交易去向

    private String contractNo;//交易合同号

    private Long accountBalance;//账户余额

    private String presentState;//提现状态

    private Integer revision;//乐观锁

}
