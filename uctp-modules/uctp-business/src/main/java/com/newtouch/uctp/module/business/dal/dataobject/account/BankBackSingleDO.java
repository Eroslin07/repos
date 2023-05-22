package com.newtouch.uctp.module.business.dal.dataobject.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import lombok.*;

@TableName("uctp_bank_back_single")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankBackSingleDO extends BaseDO {

    @TableId
    private Long id;

    private String type;

    private String associationType;

    private String url;

    private Long associationId;

    private String accountNo;

    private Integer revision;

    public enum Type {
        CASH_RECHARGE("1", "保证金充值"),

        COLLECT_PAYMENT("2", "收车款支付"),

        CASH_DEPOSIT("3", "保证金提现"),

        SELL_PAYMENT("4", "卖车款支付"),

        PROFIT_DEPOSIT("5", "利润提现");

        private String type;

        private String text;

        Type(String type, String text) {
            this.type = type;
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }
}
