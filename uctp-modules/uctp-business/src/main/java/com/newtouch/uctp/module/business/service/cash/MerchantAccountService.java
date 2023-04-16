package com.newtouch.uctp.module.business.service.cash;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;

public interface MerchantAccountService {

    MerchantAccountDO queryByAccountNo(String accountNo);

    int rechargeCash(String accountNo, Integer tranAmount, Integer revision);

    int changeCash(String accountNo, Integer tranAmount, Integer revision, String tradeType);

    /**
     * 加锁更新
     *
     * @param account 商户账户信息
     * @return 成功更新的记录数
     */
    int updateByLock(MerchantAccountDO account);

    /**
     * 商户账户开户
     *
     * @param account
     * @return
     */
    boolean accountOpen(MerchantAccountDO account);
}
