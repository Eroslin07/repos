package com.newtouch.uctp.module.business.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosBindReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;

public interface MerchantAccountService extends IService<MerchantAccountDO> {

    MerchantAccountDO queryByAccountNo(String accountNo);

    int updateCash(String accountNo, Long tranAmount, Integer revision, int type);

    MerchantAccountDO changeCash(String accountNo, Long tranAmount, Integer revision, String tradeType);

    /**
     * 加锁更新
     *
     * @param account 商户账户信息
     * @return 成功更新的记录数
     */
    int updateByLock(MerchantAccountDO account);

    /**
     * 查询商户虚拟账户资产详情
     *
     * @return 商户虚拟账户资产详情
     */
    MerchantAccountRespVO get(Long merchantId);


    /**
     * 绑定POS机商户编号
     */
    String bindPosMrhNo(PosBindReqVO req);


    /**
     * @param merchantId
     * @return
     */
    String getPosMrhNoInfo(Long merchantId);

    /**
     * @param req
     * @return
     */
    String posMrhNoModify(PosBindReqVO req);
}
