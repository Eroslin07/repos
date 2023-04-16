package com.newtouch.uctp.module.business.api.account.dto;

import lombok.Data;

@Data
public class TransferAccountDTO {

    private String payerName;

    private String payerBankName;

    private String payerBankAccount;

    private String payeeName;

    private String payeeBankName;

    private String payeeBankAccount;

    private String amount;
}
