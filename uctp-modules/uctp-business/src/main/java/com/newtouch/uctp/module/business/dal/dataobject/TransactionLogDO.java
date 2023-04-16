package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("uctp_transaction_log")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLogDO implements Serializable {

    private Long id;

    private String tranId;

    private LocalDateTime tranBeginTime;

    private LocalDateTime tranEndTime;

    private String tranRequest;

    private String tranResponse;

    private String tranStatus;
}
