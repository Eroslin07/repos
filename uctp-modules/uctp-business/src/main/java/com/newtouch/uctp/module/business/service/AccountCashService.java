package com.newtouch.uctp.module.business.service;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.AccountCashRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.CashDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
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
     * @return 返回我的保证金页面模型数据 {@link AccountCashService#detail}
     */
    AccountCashRespVO recharge(TransactionRecordReqVO transactionRecordReqVO);

    /**
     * 保证金提取
     *
     * @param transactionRecordReqVO 账户号/交易金额/版本号
     * @return 返回我的保证金页面模型数据 {@link AccountCashService#detail}
     */
    AccountCashRespVO withdraw(TransactionRecordReqVO transactionRecordReqVO);

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

}
