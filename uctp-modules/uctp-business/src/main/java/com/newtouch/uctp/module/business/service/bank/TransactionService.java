package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.module.business.enums.bank.BankSubAccountType;
import com.newtouch.uctp.module.business.service.bank.request.NominalAccountRequest;
import com.newtouch.uctp.module.business.service.bank.request.TechAddressesRequest;
import com.newtouch.uctp.module.business.service.bank.response.InnerTransferResponse;
import com.newtouch.uctp.module.business.service.bank.response.NominalAccountResponse;
import com.newtouch.uctp.module.business.service.bank.response.TechAddressesResponse;

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
    NominalAccountResponse nominalAccountGenerate(NominalAccountRequest nominalAccountRequest);

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
     * @param accountNo         平台账户号
     * @param contractNo        合同号
     * @param outSubAccountType 转出子账户类型
     * @param inSubAccountType  转入子账户类型
     * @param tranAmount        交易金额
     * @param remark            备注
     * @return 银行响应结果
     */
    InnerTransferResponse innerTransfer(String accountNo,
                                        String contractNo,
                                        BankSubAccountType outSubAccountType,
                                        BankSubAccountType inSubAccountType,
                                        Long tranAmount,
                                        String remark);

    /**
     * 不明入金清分
     */
    String unKnowClearing(String contractNo);

    /**
     * 商户充值时使用银行APP支付，通过链接唤起银行端APP
     *
     * 账链接生成交易
     */
    TechAddressesResponse techAddressesGenerate(TechAddressesRequest techAddressesRequest);
}
