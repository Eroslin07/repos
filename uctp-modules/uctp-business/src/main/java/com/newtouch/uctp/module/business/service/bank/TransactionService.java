package com.newtouch.uctp.module.business.service.bank;

import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.DepositsNotificationRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.service.bank.dto.OutGoldDTO;
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
     * 支付通知接口
     * C端买车款：
     * 1、POS机支付通知
     * 2、银行转账支付通知
     * 商户充值：
     * 1、银行APP支付通知
     */
    DepositsNotificationRespVO noticePaymentResult(DepositsNotificationReqVO request) throws BankException;

//    /**
//     * 订单支付
//     *
//     * @param contractNo 交易合同号=订单号
//     * @return
//     */
//    String orderPayment(String contractNo);
//
//    /**
//     * 订单支付状态查询
//     *
//     * @param contractNo 交易合同号=订单号
//     * @return
//     */
//    TransactionRecordDO orderPayStatus(String contractNo);

    /**
     * 商户银行子账户创建
     *
     * @return 银行子账户号
     */
    NominalAccountResponse nominalAccountGenerate(NominalAccountRequest nominalAccountRequest);

    /**
     * 银行出金
     * * 支付收车款：商户保证金子账户
     *
     * @param outGoldDTO
     * @param bank
     * @return
     */
    Boolean outGold(OutGoldDTO outGoldDTO, MerchantBankDO bank);

    /**
     * 子账号互转
     *
     * @param accountNo  平台账户号
     * @param contractNo 合同号
     * @param tranType   交易类型
     * @param tranAmount 交易金额
     * @param remark     备注
     * @return 银行响应结果
     */
    InnerTransferResponse innerTransfer(String accountNo,
                                        String contractNo,
                                        String tranType,
                                        Long tranAmount,
                                        String remark);

    /**
     * 不明入金清分
     */
    String unKnowClearing(String contractNo);

    /**
     * 商户充值时使用银行APP支付，通过链接唤起银行端APP
     * <p>
     * 账链接生成交易
     */
    TechAddressesResponse techAddressesGenerate(TechAddressesRequest techAddressesRequest);


    /**
     * 支付失败
     * 1、主要用于收车款支付 商户保证金子账户支付C端卖车人失败场景动作
     * 2、方法内创建支付失败流程
     */
    void collectPaymentFailed();

    /**
     * 支付成功
     * 1、主要用于收车款支付 商户保证金子账户支付C端卖车人成功场景动作
     * 2、方法内创建开票流程
     */
    void collectPaymentSuccess();
}
