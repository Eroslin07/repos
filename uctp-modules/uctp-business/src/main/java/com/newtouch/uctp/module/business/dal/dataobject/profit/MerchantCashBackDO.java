package com.newtouch.uctp.module.business.dal.dataobject.profit;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName("uctp_merchant_cash_back")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCashBackDO extends BaseDO {

    @TableId
    private Long id; // 主键标识
    private String accountNo; // 商户账户号
    private String contractNo; // 交易合同号
    private String type; // 类型（如：待回填，利润抵扣）
    private String typeText; // 类型中文名称
    private LocalDateTime occurredTime; // 发生时间
    private Long amount; // 金额
    private String tradeTo; // 交易去向
    private String tradeToText; // 交易去向中文名称
}
