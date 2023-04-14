package com.newtouch.uctp.module.business.dal.dataobject.cash;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

@TableName("uctp_merchant_account")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAccountDO extends BaseDO {

    @TableId
	private Long id;//账户主键标识

	private String accountNo;//账户号

	private Integer cash;//保证金

	private Integer profit;//利润

	private Integer freezeCash;//保证金-冻结余额

	private Integer availableCash;//保证金-可用余额

	private Integer freezeProfit;//利润-冻结余额

	private Long tenantId;//商户标识

	private Integer revision;//乐观锁

}
