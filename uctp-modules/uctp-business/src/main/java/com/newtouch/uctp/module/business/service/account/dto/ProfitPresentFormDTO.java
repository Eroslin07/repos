package com.newtouch.uctp.module.business.service.account.dto;

import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitPresentInvoiceRespVO;
import lombok.Data;

import java.util.List;

@Data
public class ProfitPresentFormDTO {
    private String profitId; // 利润提现id
    private String merchantId; // 商户标识
    private String merchantName; // 商户名称
    private String telNo; // 电话号码
    private Long amount; // 提取金额
    private Long balanceAmount; // 剩余金额
    private String bankNo; // 银行卡号
    private String bankName; // 开户名称
    private String bankOfDeposit; // 开户行
    private List<ProfitPresentInvoiceRespVO> invoiceFiles; // 发票文件清单
    private List<ProfitPresentFormDetailDTO> profitDetails; // 利润明细
}
