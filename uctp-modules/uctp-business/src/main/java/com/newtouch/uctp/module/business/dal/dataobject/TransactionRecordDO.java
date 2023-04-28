package com.newtouch.uctp.module.business.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@TableName("uctp_transaction_record")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecordDO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String tranNo;

    private String tranType;

    private String contractNo;

    private String payerName;

    private String payerBankName;

    private String payerBankAccount;

    private String payeeName;

    private String payeeBankName;

    private String payeeBankAccount;

    private String approveAccount;

    private Long tranAmount;

    private String tranStatus;

    private String bankResultCode;

    private String bankResultReason;
}
