package com.newtouch.uctp.module.business.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashRespVo;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantCashMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.account.MerchantCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

@Service
@Validated
@Slf4j
public class MerchantCashServiceImpl  extends ServiceImpl<MerchantCashMapper, MerchantCashDO> implements MerchantCashService {

    @Resource
    private MerchantCashMapper merchantCashMapper;


    @Override
    public PageResult<MerchantCashDO> queryPageByAccountNo(MerchantCashReqVO merchantCashReq) {
        return merchantCashMapper.queryPageByAccountNo(merchantCashReq);
    }

    @Override
    public PageResult<MerchantCashRespVo> queryPageByAccountNos(MerchantCashReqVO merchantCashReq) {
        Page<MerchantCashRespVo> page = new Page<>(merchantCashReq.getPageNo(), merchantCashReq.getPageSize());
        String param="";
        if (merchantCashReq.getType() == 2) {
            //冻结明细 getTradeType
            param=AccountConstants.TRADE_TYPE_PREEMPTION+","+ AccountConstants.TRADE_TYPE_RELEASE+AccountConstants.TRADE_TYPE_WITHDRAWING;
        }

        if (merchantCashReq.getType() == 3) {
            //支出明细 getProfitLossType
            param=AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT;
        }

        if (merchantCashReq.getType() == 4) {
            //收入明细
            param=AccountConstants.PROFIT_LOSS_TYPE_INCOME;
        }
        page = merchantCashMapper.selectAppHomePage(page,param, merchantCashReq);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public int insert(MerchantCashDO merchantCashDO) {
        return merchantCashMapper.insert(merchantCashDO);
    }

    @Override
    @Transactional
    public MerchantCashDO insertCash(MerchantAccountDO merchantAccountDO, Long tranAmount, String type, String tradeRecordNo, String contractNo) {
        MerchantCashDO merchantCashDO = new MerchantCashDO();

        merchantCashDO.setAccountBalance(merchantAccountDO.getCash() + merchantAccountDO.getProfit());
        merchantCashDO.setRevision(1);
        merchantCashDO.setPayAmount(tranAmount);
        merchantCashDO.setTradeType(type);
        merchantCashDO.setAccountId(merchantAccountDO.getId());
        merchantCashDO.setAccountNo(merchantAccountDO.getAccountNo());
        merchantCashDO.setDeleted(Boolean.FALSE);

        switch (type) {
            case AccountConstants.TRADE_TYPE_RECHARGE:
                merchantCashDO.setPayChannel(AccountConstants.PAY_CHANNEL_DEFAULT);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_INCOME);
                merchantCashDO.setTranRecordNo(tradeRecordNo);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                break;
            case AccountConstants.TRADE_TYPE_WITHDRAW:
                merchantCashDO.setPayChannel(AccountConstants.PAY_CHANNEL_DEFAULT);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
                merchantCashDO.setTranRecordNo(tradeRecordNo);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                merchantCashDO.setPresentState(AccountConstants.PRESENT_CASH_SUCCESS);
                break;
            case AccountConstants.TRADE_TYPE_PREEMPTION:
                merchantCashDO.setPayChannel(AccountConstants.PAY_CHANNEL_PLATFORM);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_BUY_CARS);
                merchantCashDO.setContractNo(contractNo);
                break;
            case AccountConstants.TRADE_TYPE_BACK:
            case AccountConstants.TRADE_TYPE_PROFIT_BACK:
            case AccountConstants.TRADE_TYPE_RELEASE:
                merchantCashDO.setPayChannel(AccountConstants.PAY_CHANNEL_PLATFORM);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_INCOME);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                merchantCashDO.setContractNo(contractNo);
                break;
            case AccountConstants.TRADE_TYPE_WITHDRAWING:
                merchantCashDO.setPayChannel(AccountConstants.PAY_CHANNEL_DEFAULT);
                merchantCashDO.setProfitLossType(AccountConstants.PROFIT_LOSS_TYPE_DISBURSEMENT);
                merchantCashDO.setTranRecordNo(tradeRecordNo);
                merchantCashDO.setTradeTo(AccountConstants.TRADE_TO_MY_CASH);
                merchantCashDO.setPresentState(AccountConstants.PRESENT_CASH_PROCESSING);
                break;
        }
        log.info("新增保证金变更记录:{}", JSON.toJSONString(merchantCashDO));
        this.insert(merchantCashDO);
        return merchantCashDO;
    }

    @Override
    public MerchantCashDO queryContractNoAmount(String contractNo, List<String> tradeTypes) {
        return merchantCashMapper.queryContractNoAmount(contractNo, tradeTypes);
    }

    @Override
    public void updateCashDeduction(String contractNo) {
        merchantCashMapper.update(new MerchantCashDO(), new LambdaUpdateWrapper<MerchantCashDO>()
                .set(MerchantCashDO::getTradeType,AccountConstants.TRADE_TYPE_DEDUCTION)
                .eq(MerchantCashDO::getContractNo,contractNo)
                .eq(MerchantCashDO::getTradeType,AccountConstants.TRADE_TYPE_PREEMPTION));
    }
}
