package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.AccountCashRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.CashDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.AccountCashService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import com.newtouch.uctp.module.business.service.cash.MerchantCashService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class AccountCashServiceImpl implements AccountCashService {

    private final MerchantAccountService merchantAccountService;
    private final MerchantCashService merchantCashService;

    //保证金详情
    @Override
    public AccountCashRespVO detail(String accountNo) {

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        if (merchantAccountDO == null || StringUtils.isEmpty(merchantAccountDO.getAccountNo())) {
            throw new ServiceException(AccountConstants.ERROR_CODE_ACCOUNT_NOT_FOUND, AccountConstants.ERROR_MESSAGE_ACCOUNT_NOT_FOUND);
        }

        MerchantCashReqVO merchantCashReqVO = new MerchantCashReqVO().setAccountNo(accountNo);
        merchantCashReqVO.setPageSize(5);
        PageResult<MerchantCashDO> merchantCashDOPageResult = merchantCashService.queryPageByAccountNo(merchantCashReqVO);
        return AccountCashRespVO.build(merchantAccountDO, merchantCashDOPageResult.getList());
    }

    //保证金明交易明细查询
    @Override
    public PageResult<CashDetailRespVO> list(MerchantCashReqVO merchantCashReqVO) {
        PageResult<MerchantCashDO> merchantCashDOPageResult = merchantCashService.queryPageByAccountNo(merchantCashReqVO);

        PageResult<CashDetailRespVO> page = new PageResult<>();
        page.setTotal(merchantCashDOPageResult.getTotal());
        List<MerchantCashDO> list = merchantCashDOPageResult.getList();
        if (CollectionUtil.isNotEmpty(list)) {
            page.setList(list.stream().map(CashDetailRespVO::build).collect(Collectors.toList()));
        }
        return page;
    }

    //保证金充值
    @Override
    @Transactional
    public AccountCashRespVO recharge(TransactionRecordReqVO transactionRecordReqVO) {

        //增加保证金金额，版本号加1
        int count = merchantAccountService.rechargeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount());

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());

        boolean flag = Boolean.TRUE;
        //TODO: 调用转账接口及uctp_transaction_log日志记录
        if (!flag) {
            //转账接口调用失败
            throw new ServiceException(AccountConstants.ERROR_CODE_INTERFACE_CALL_FAILURE, AccountConstants.ERROR_MESSAGE_INTERFACE_CALL_FAILURE);
        }

        //新增保证金变动记录
        String tradeRecordNo = "" + System.currentTimeMillis();//支付流水
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_RECHARGE, tradeRecordNo, null);

        if (count <= 0) {
            //数据修改失败
            throw new ServiceException(AccountConstants.ERROR_CODE_REVERSION_ERROR, AccountConstants.ERROR_MESSAGE_REVERSION_ERROR);
        }
        return this.detail(transactionRecordReqVO.getAccountNo());
    }

    //保证金提取
    @Override
    @Transactional
    public AccountCashRespVO withdraw(TransactionRecordReqVO transactionRecordReqVO) {
        //扣除保证金金额，版本号加1
        merchantAccountService.changeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision(), AccountConstants.TRADE_TYPE_WITHDRAW);

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());

        boolean flag = Boolean.TRUE;
        //TODO: 调用转账接口及uctp_transaction_log日志记录
        if (!flag) {
            //转账接口调用失败
            throw new ServiceException(AccountConstants.ERROR_CODE_INTERFACE_CALL_FAILURE, AccountConstants.ERROR_MESSAGE_INTERFACE_CALL_FAILURE);
        }

        //新增保证金变动记录
        String tradeRecordNo = "" + System.currentTimeMillis();//支付流水
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_WITHDRAW, tradeRecordNo, null);
        return this.detail(transactionRecordReqVO.getAccountNo());
    }

    //保证金预占
    @Override
    @Transactional
    public Boolean reserve(TransactionRecordReqVO transactionRecordReqVO) {
        //保证金预占，版本号加1
        int count = merchantAccountService.changeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision(), AccountConstants.TRADE_TYPE_PREEMPTION);
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_PREEMPTION, null, transactionRecordReqVO.getContractNo());
        //todo:保证金预占是否需要银行介入？
        return Boolean.TRUE;
    }


}
