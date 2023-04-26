package com.newtouch.uctp.module.business.service.bank;

/**
 * 银行交易服务
 */
public interface TransactionService {

    /**
     * TODO
     * 商户充值保证金时的 APP唤起链接哪里获取
     * C端买车款入金后的交易结果
     *
     */

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
     * 订单支付
     *
     * @param contractNo 交易合同号=订单号
     * @return
     */
    String orderPayment(String contractNo);

    /**
     * 订单支付状态查询
     *
     * @param contractNo 交易合同号=订单号
     * @return
     */
    String orderPayStatus(String contractNo);

    /**
     * 商户银行子账户创建
     *
     * @return 银行子账户号
     */
    String nominalAccountGenerate();

    /**
     * 银行出金
     * 支付收车款：商户保证金子账户
     *
     * @return
     */
    String outGold();

    /**
     * 子账号互转
     *
     * @return
     */
    String innerTransfer();

    /**
     * 不明入金清分
     */
    String unKnowClearing(String contractNo);
}
