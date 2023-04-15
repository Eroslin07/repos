package com.newtouch.uctp.module.business.service.cash;

import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;

public interface MerchantAccountService {

    MerchantAccountDO queryByAccountNo(String accountNo);

    int rechargeCash(String accountNo, Integer tranAmount);

    int changeCash(String accountNo, Integer tranAmount, Integer revision, String tradeType);
}
