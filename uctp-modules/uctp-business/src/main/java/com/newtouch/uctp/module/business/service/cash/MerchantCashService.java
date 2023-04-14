package com.newtouch.uctp.module.business.service.cash;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;


public interface MerchantCashService {

    PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq);

    int insert(MerchantCashDO merchantCashDO);

    int insertCash(MerchantAccountDO merchantAccountDO, Integer tranAmount, String type, String tradeRecordNo, String contractNo);
}
