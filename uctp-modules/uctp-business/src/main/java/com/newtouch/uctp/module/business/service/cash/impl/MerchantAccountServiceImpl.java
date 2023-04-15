package com.newtouch.uctp.module.business.service.cash.impl;

import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
@Slf4j
public class MerchantAccountServiceImpl implements MerchantAccountService {

    @Resource
    private MerchantAccountMapper merchantAccountMapper;

    @Override
    public MerchantAccountDO queryByAccountNo(String accountNo) {
        return merchantAccountMapper.queryByAccountNo(accountNo);
    }

    @Override
    @Transactional
    public int rechargeCash(String accountNo, Integer tranAmount) {
        return merchantAccountMapper.rechargeCash(accountNo, tranAmount);
    }

    @Override
    @Transactional
    public int changeCash(String accountNo, Integer tranAmount, Integer revision, String tradeType) {

        MerchantAccountDO merchantAccountDO = merchantAccountMapper.selectForUpdateByAccountNo(accountNo);
        if (merchantAccountDO == null || merchantAccountDO.getAvailableCash() < tranAmount) {
            //可用余额不足
            throw new ServiceException(AccountConstants.ERROR_CODE_INSUFFICIENT_AVAILABLE_BALANCE, AccountConstants.ERROR_MESSAGE_INSUFFICIENT_AVAILABLE_BALANCE);
        }

        if (!revision.equals(merchantAccountDO.getRevision())) {
            //版本号错误
            throw new ServiceException(AccountConstants.ERROR_CODE_REVERSION_ERROR, AccountConstants.ERROR_MESSAGE_REVERSION_ERROR);
        }

        switch (tradeType) {
            case AccountConstants.TRADE_TYPE_WITHDRAW:
                merchantAccountDO.setCash(merchantAccountDO.getCash() - tranAmount);
                merchantAccountDO.setAvailableCash(merchantAccountDO.getAvailableCash() - tranAmount);
                break;
            case AccountConstants.TRADE_TYPE_PREEMPTION:
                merchantAccountDO.setCash(merchantAccountDO.getCash() - tranAmount);
                merchantAccountDO.setAvailableCash(merchantAccountDO.getAvailableCash() - tranAmount);
                merchantAccountDO.setFreezeCash(merchantAccountDO.getFreezeCash() + tranAmount);
                break;
        }
        return merchantAccountMapper.updateById(merchantAccountDO);
    }

}
