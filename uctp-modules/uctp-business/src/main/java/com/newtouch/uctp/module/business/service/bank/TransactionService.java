package com.newtouch.uctp.module.business.service.bank;

/**
 * 银行交易服务
 */
public interface TransactionService {

    /**
     * 支付通知接口
     * C端买车款：
     * 1、POS机支付通知
     * 2、银行转账支付通知
     * 商户充值：
     * 1、银行APP支付通知
     */
    void noticePaymentResult();

    /**
     * 商户银行子账户创建
     *
     * @return 银行子账户号
     */
    String nominalAccountCreate();

    /**
     * 银行出金
     * 支付收车款：商户保证金子账户
     *
     * @return
     */
    String outGold();
}
