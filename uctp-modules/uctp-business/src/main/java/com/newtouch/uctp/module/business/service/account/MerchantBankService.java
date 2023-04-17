package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;

public interface MerchantBankService {

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    MerchantBankDO getById(Long id);

}
