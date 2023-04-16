package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.AccountCashRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.CashDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashReqVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantPresentStatusRecordMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.AccountCashService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import com.newtouch.uctp.module.business.service.cash.MerchantCashService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.newtouch.uctp.module.business.enums.AccountConstants.PRESENT_TYPE_PROFIT;

@Service
@Validated
@RequiredArgsConstructor
public class AccountCashServiceImpl implements AccountCashService {

    private final MerchantAccountService merchantAccountService;
    private final MerchantCashService merchantCashService;
    private final MerchantPresentStatusRecordMapper merchantPresentStatusRecordMapper;
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
        int count = merchantAccountService.rechargeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision());

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
        MerchantCashDO merchantCashDO = merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_WITHDRAW, tradeRecordNo, null);

        PresentStatusRecordDO presentStatusRecordDO = buildPresentStatusRecordDO(merchantCashDO.getId(),AccountConstants.PRESENT_STATUS_CASH_APPLY);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO);
        PresentStatusRecordDO presentStatusRecordDO1 = buildPresentStatusRecordDO(merchantCashDO.getId(),AccountConstants.PRESENT_STATUS_CASH_PROCESSING);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO1);
        PresentStatusRecordDO presentStatusRecordDO2 = buildPresentStatusRecordDO(merchantCashDO.getId(),AccountConstants.PRESENT_STATUS_CASH_SUCCESS);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO2);

        return this.detail(transactionRecordReqVO.getAccountNo());
    }

    private PresentStatusRecordDO buildPresentStatusRecordDO(Long cashId, String presentStatus) {
        PresentStatusRecordDO presentStatusRecord = new PresentStatusRecordDO();
        presentStatusRecord.setStatus(presentStatus);
        presentStatusRecord.setPresentType(AccountConstants.PRESENT_TYPE_CASH);
        presentStatusRecord.setOccurredTime(LocalDateTime.now());
        presentStatusRecord.setDeleted(Boolean.FALSE);
        presentStatusRecord.setPresentNo(cashId);
        return presentStatusRecord;
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
        return count > 0;
    }

    //保证金实占
    @Override
    @Transactional
    public Boolean deduction(TransactionRecordReqVO transactionRecordReqVO) {
        String contractNo = transactionRecordReqVO.getContractNo();
        //查询合同号预占金额
        MerchantCashDO merchantCashDO = merchantCashService.queryContractNoAmount(contractNo, Collections.singletonList(AccountConstants.TRADE_TYPE_PREEMPTION));
        String accountNo = merchantCashDO.getAccountNo();
        Integer payAmount = merchantCashDO.getPayAmount();
        if (StringUtils.isEmpty(accountNo) || payAmount == null || payAmount <= 0) {
            throw new ServiceException(AccountConstants.ERROR_CODE_CONTRACT_NO_NOT_FOUND, AccountConstants.ERROR_MESSAGE_CONTRACT_NO_NOT_FOUND);
        }

        //冻结金额扣除，版本号+1
        int count = merchantAccountService.changeCash(accountNo, payAmount, null, AccountConstants.TRADE_TYPE_DEDUCTION);

        //新增保证金变动记录
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        merchantCashService.insertCash(merchantAccountDO, payAmount, AccountConstants.TRADE_TYPE_DEDUCTION, null, contractNo);

        //todo：是否调用银行接口扣除金额
        return count > 0;
    }

    //待回填保证金
    @Override
    public Integer difference(TransactionRecordReqVO transactionRecordReqVO) {
        //查询合同号预占金额
        MerchantCashDO reserveCashDO = merchantCashService.queryContractNoAmount(transactionRecordReqVO.getContractNo(), Collections.singletonList(AccountConstants.TRADE_TYPE_PREEMPTION));
        MerchantCashDO backCashDO = merchantCashService.queryContractNoAmount(transactionRecordReqVO.getContractNo(), Arrays.asList(AccountConstants.TRADE_TYPE_BACK,AccountConstants.TRADE_TYPE_PROFIT_BACK));
        return reserveCashDO.getPayAmount() - backCashDO.getPayAmount();
    }

    //保证金回填
    @Override
    public Boolean back(TransactionRecordReqVO transactionRecordReqVO) {
        //增加保证金金额，版本号加1
        int count = merchantAccountService.rechargeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision());

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());
        //TODO: 是否调用转账接口

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_BACK, null, transactionRecordReqVO.getContractNo());
        return count > 0;
    }

    //保证金回填-利润
    @Override
    public Boolean profitBack(TransactionRecordReqVO transactionRecordReqVO) {
        //增加保证金金额，版本号加1
        int count = merchantAccountService.rechargeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision());

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());
        //TODO: 是否调用转账接口

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_PROFIT_BACK, null, transactionRecordReqVO.getContractNo());
        return count > 0;
    }


}
