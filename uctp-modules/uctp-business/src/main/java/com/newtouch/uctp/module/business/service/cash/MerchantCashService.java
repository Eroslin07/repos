package com.newtouch.uctp.module.business.service.cash;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;

import java.util.List;


public interface MerchantCashService {

    PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq);

    int insert(MerchantCashDO merchantCashDO);

    MerchantCashDO insertCash(MerchantAccountDO merchantAccountDO, Integer tranAmount, String type, String tradeRecordNo, String contractNo);

    MerchantCashDO queryContractNoAmount(String contractNo, List<String> tradeTypes);

    void updateCashDeduction(String contractNo);
}