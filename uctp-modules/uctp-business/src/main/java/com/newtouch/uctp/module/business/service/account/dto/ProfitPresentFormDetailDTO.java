package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;

@Data
public class ProfitPresentFormDetailDTO {
    private int idx; // 序号
    private String merchantName; // 商户名称
    private String category; // 分类
    private String telNo; // 手机号
    private Long vehicleReceiptAmount; // 收车金额
    private Long carSalesAmount; // 卖车金额
    private Long feeTotalAmount; // 费用总金额
    private Long amount; // 利润金额
    private String tradeTime; // 交易时间
    private Long balanceAmount; // 商户余额
}
