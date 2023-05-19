package com.newtouch.uctp.module.business.service.bank.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POS商户未支付订单响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosNonPayOrderResponse {

    /**
     * 车架号
     */
    private String vin;

    /**
     * 订单号 - 平台交易合同号
     */
    private String orderNo;

    /**
     * 支付金额
     */
    private Double amount;
}
