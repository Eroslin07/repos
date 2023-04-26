package com.newtouch.uctp.module.business.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;

public interface MerchantBankService extends IService<MerchantBankDO> {

    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    MerchantBankDO getById(Long id);
}
