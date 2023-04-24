package com.newtouch.uctp.module.business.service.account.dto;

import lombok.Data;

@Data
public class ProfitPresentFormDetailDTO {
    private int idx; // 序号
    private String merchantName; // 商户名称
    private String category; // 分类
    private String telNo; // 手机号
    private Integer vehicleReceiptAmount; // 收车金额
    private Integer carSalesAmount; // 卖车金额
    private Integer feeTotalAmount; // 费用总金额
    private Integer amount; // 利润金额
    private String tradeTime; // 交易时间
    private Integer balanceAmount; // 商户余额
}
