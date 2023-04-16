package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.module.business.api.account.dto.TransferAccountDTO;

/**
 * 交易服务
 */
public interface TransactionService {

    /**
     * 银行转账交易状态
     */
    void transactionStatus(String tranNo);

    /**
     * 银行转账 对外支付或内部转账
     * 保证金-充值 to 个人账户 行内转账 实时到账
     * 保证金-提现 to 个人账户 行内转账 实时到账
     * 利润-提现 to 企业账户 行内转装或他行账户 具体看银行处理时间
     * 收车款-转账 to 卖车方 行内转装或他行账户 具体看银行处理时间
     */
    void transferAccount(TransferAccountDTO transferAccountDTO);

    /**
     * 支付状态查询 ToDO 待确认支付渠道具体再确认参数
     * 买车款-支付 to 银行账户 具体看支付渠道时间
     */
    void payStatus();

    /**
     * 线下转账充值保证金
     * 商户个人账户 -> 银行账户
     */
    void transferAccountToCash();

    /**
     * 通过交易合同号查询交易流水号
     *
     * @return
     */
    String tranNoByContractNo();
}
