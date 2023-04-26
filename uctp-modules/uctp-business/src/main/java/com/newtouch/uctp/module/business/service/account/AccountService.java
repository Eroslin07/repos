package com.newtouch.uctp.module.business.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;

public interface AccountService extends IService<MerchantAccountDO> {

    boolean openAccount(AccountDTO accountDTO);
}
