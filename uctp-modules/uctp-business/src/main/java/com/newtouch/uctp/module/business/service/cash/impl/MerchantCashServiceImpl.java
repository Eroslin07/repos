package com.newtouch.uctp.module.business.service.cash.impl;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantCashMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.cash.MerchantCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
@Slf4j
public class MerchantCashServiceImpl implements MerchantCashService {

    @Resource
    private MerchantCashMapper merchantCashMapper;


    @Override
    public PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq) {
        return merchantCashMapper.queryPageByAccountNo(merchantCashReq);
    }

    @Override
    public int insert(MerchantCashDO merchantCashDO) {
        return merchantCashMapper.insert(merchantCashDO);
    }

    @Override
    @Transactional
    public int insertCash(MerchantAccountDO merchantAccountDO, Integer tranAmount, String type, String tradeRecordNo, String contractNo) {
        MerchantCashDO merchantCashDO = new MerchantCashDO();

        merchantCashDO.setAccountBalance(merchantAccountDO.getCash() + merchantAccountDO.getProfit());
        merchantCashDO.setRevision(1);
        merchantCashDO.setPayAmount(tranAmount);
        merchantCashDO.setTradeType(type);
        merchantCashDO.setAccountId(merchantAccountDO.getId());
        merchantCashDO.setAccountNo(merchantAccountDO.getAccountNo());

        switch (type) {
            case AccountConstants.TRADE_TYPE_RECHARGE:
                merchantCashDO.setPayChannel(AccountConstants.DEFAULT_PAY_CHANNEL);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_INCOME);
                merchantCashDO.setTradeRecordNo(tradeRecordNo);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                break;
            case AccountConstants.TRADE_TYPE_WITHDRAW:
                merchantCashDO.setPayChannel(AccountConstants.DEFAULT_PAY_CHANNEL);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
                merchantCashDO.setTradeRecordNo(tradeRecordNo);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                break;
            case AccountConstants.TRADE_TYPE_PREEMPTION:
                merchantCashDO.setPayChannel(AccountConstants.DEFAULT_PAY_CHANNEL);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                merchantCashDO.setContractNo(contractNo);
                break;
        }

        return this.insert(merchantCashDO);
    }
}
