package com.newtouch.uctp.module.business.dal.dataobject.profit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@TableName("uctp_merchant_profit")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProfitDO extends BaseDO {

    @TableId
    private Long id; // 主键标识
    private Long accountId; // 账户标识（暂不写值）
    private String accountNo; // 商户账户号
    private String profitLossType; // 损益类型
    private String profitLossTypeText; // 损益类型中文名称
    private LocalDateTime tradeTime; // 交易时间
    private String tradeType; // 交易类型
    private String tradeTypeText; // 交易类型中文名称
    private String tradeTo; // 交易去向
    private String tradeToText; // 交易去向中文名称
    private String contractNo; // 交易合同号
    private Long profit; // 利润
    private Long profitBalance; // 利润余额
    private Long cashBack; // 待回填保证金额度
    private Long vehicleReceiptAmount; // 收车款
    private String bankNo; // 提现银行卡号
    private String bankName;// 提现银行名称
    private String presentState;// 提现状态
    private String presentStateText;// 提现状态中文名称
    private Integer revision; // 乐观锁
    // 提现状态记录列表
    @TableField(exist = false)
    private List<PresentStatusRecordDO> presentStatusRecords;

}
