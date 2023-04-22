package com.newtouch.uctp.module.business.service.account;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackRespVO;

public interface MerchantCashBackService {

    /**
     * 待回填保证金明细分页查询
     * @param accountNo 商户账号
     * @param query 查询对接
     * @return 分页的待回填保证金明细
     */
    PageResult<CashBackRespVO> list(String accountNo, CashBackReqVO query);

    /**
     * 查询待回填保证金明细详情
     * @param accountNo 商户账号
     * @param id 待回填保证金ID
     * @return 待回填保证金详情
     */
    CashBackRespVO detail(String accountNo, Long id);
}
