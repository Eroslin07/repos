package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.*;
import org.springframework.stereotype.Service;

@Service
public interface AccountCashService {

    /**
     * 保证金详情查询
     *
     * @param accountNo 商户账户号
     * @return AccountCashRespVO 账户保证金信息及保证及交易流水前五条数据
     */
    AccountCashRespVO detail(String accountNo);

    /**
     * 保证金明交易明细分页查询
     *
     * @param merchantCashReqVO 商户账户号及分页参数
     * @return PageResult<CashDetailRespVO> 分页查询数据详情
     */
    PageResult<CashDetailRespVO> list(MerchantCashReqVO merchantCashReqVO);

    /**
     * 保证金充值
     *
     * @param transactionRecordReqVO 账户号/交易金额/版本号
     * @return
     */
    CashRechargeRespVO recharge(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 线下版保证金充值 确认到账 todo helong 沟通交互
     *
     * @return
     */
    Boolean rechargeSuccess();

    /**
     * 保证金提取
     *
     * @param transactionRecordReqVO 账户号/交易金额/版本号
     * @return 返回我的保证金页面模型数据 {@link AccountCashService#detail}
     */
    AccountCashRespVO withdraw(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金提取-扣除
     *
     * @param tradeRecordNo 银行流水号
     * @return 扣除结果
     */
    Boolean changeWithdrawState(String tradeRecordNo);

    /**
     * 保证金预占-冻结
     *
     * @param transactionRecordReqVO 账户号/交易金额/交易合同号
     * @return 保证金预占-冻结结果：true/false
     */
    Boolean reserve(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金实占-扣除冻结
     *
     * @param transactionRecordReqVO 交易合同号
     * @return 保证金实占-扣除冻结结果：true/false
     */
    Boolean deduction(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 待回填保证金
     *
     * @param transactionRecordReqVO 账户号
     * @return 待回填保证金金额，单位-分
     */
    Long difference(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金回填
     *
     * @param transactionRecordReqVO 账户号/交易金额/交易合同号
     * @return 保证金回填结果：true/false
     */
    Boolean back(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金回填-利润
     *
     * @param transactionRecordReqVO 账户号/交易金额/交易合同号
     * @return 保证金回填-利润结果：true/false
     */
    Boolean profitBack(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金释放-冻结解冻
     *
     * @param transactionRecordReqVO 账户号
     * @return 保证金释放-冻结解冻结果：true/false
     */
    Boolean release(TransactionRecordReqVO transactionRecordReqVO);


    /**
     * 商户银行信息查询
     *
     * @param accountNo 商户账户号
     * @return MerchantBankRespVO 商户银行信息
     */
    MerchantBankRespVO bankInfo(String accountNo, String busType);
}
