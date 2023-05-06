package com.newtouch.uctp.module.business.service.contract;

/**
 * 根据合同处理商户的保证金预占、实占、释放，及利润提现业务
 * @author helong
 * @date 2023/5/6 11:15
 */
public interface MerchantMoneyService {
    /**
     * 收车时，预占保证金
     * @param contractId  （契约锁）收车合同ID
     * @return  预占成功、失败标志
     */
    Boolean reserveCash(Long contractId);

    /**
     * 收车中，释放保证金
     * @param contractId   （契约锁）收车合同ID
     * @return 成功、失败标志
     */
    Boolean releaseCash(Long contractId);

    /**
     * 收车时，实占保证金
     * @param contractId   （契约锁）收车合同ID
     * @return 成功、失败标志
     */
    Boolean deductionCash(Long contractId);
}
