package com.newtouch.uctp.module.business.dal.dataobject.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newtouch.uctp.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName("uctp_present_status_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresentStatusRecordDO extends BaseDO {

    @TableId
    private Long id; // 主键标识
    private String presentType; // 提现类型C：保证金提现P：利润提现
    private String status; // 提现状态
    private Long presentNo; // 保证金明细表标识或利润明细表标识
    private LocalDateTime occurredTime; // 状态变更时间
}
