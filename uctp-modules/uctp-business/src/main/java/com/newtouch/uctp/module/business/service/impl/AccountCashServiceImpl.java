package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.*;
import com.newtouch.uctp.module.business.controller.app.account.vo.PresentStatusRecordRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantCashDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantPresentStatusRecordMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.service.AccountCashService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import com.newtouch.uctp.module.business.service.cash.MerchantCashService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class AccountCashServiceImpl implements AccountCashService {

    @Resource
    private MerchantAccountService merchantAccountService;
    @Resource
    private MerchantCashService merchantCashService;
    @Resource
    private MerchantPresentStatusRecordMapper merchantPresentStatusRecordMapper;
    @Autowired
    @Lazy
    private TransactionService transactionService;
    @Resource
    private MerchantBankService merchantBankService;

    //保证金详情
    @Override
    public AccountCashRespVO detail(String accountNo) {
        log.info("保证金详情查询 accountNo:{}", accountNo);
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        if (merchantAccountDO == null || StringUtils.isEmpty(merchantAccountDO.getAccountNo())) {
            log.info("保证金详情查询-未获取到当前账户信息 accountNo:{}", accountNo);
            throw new ServiceException(AccountConstants.ERROR_CODE_ACCOUNT_NOT_FOUND, AccountConstants.ERROR_MESSAGE_ACCOUNT_NOT_FOUND);
        }

        //查询提现中冻结金额
        Long withdrawFreezeCash = merchantCashService.list(new LambdaQueryWrapperX<MerchantCashDO>()
                .eq(MerchantCashDO::getAccountNo, accountNo)
                .eq(MerchantCashDO::getTradeType, AccountConstants.TRADE_TYPE_WITHDRAWING))
                .stream().filter(x -> x != null && x.getPayAmount() != null && x.getPayAmount() > 0).mapToLong(MerchantCashDO::getPayAmount).sum();

        MerchantBankDO merchantBankDO = merchantBankService.getOne(
                new LambdaQueryWrapperX<MerchantBankDO>()
                        .eq(MerchantBankDO::getAccountNo, accountNo)
                        .eq(MerchantBankDO::getBusinessType, AccountEnum.BUSINESS_TYPE_CASH.getKey())
                        .eq(MerchantBankDO::getDeleted, Boolean.FALSE));

        MerchantCashReqVO merchantCashReqVO = new MerchantCashReqVO().setAccountNo(accountNo);
        merchantCashReqVO.setPageSize(5);
        PageResult<CashDetailRespVO> list = list(merchantCashReqVO);
        AccountCashRespVO accountCashRespVO = AccountCashRespVO.build(merchantAccountDO, list.getList());
        accountCashRespVO.setWithdrawFreezeCash(withdrawFreezeCash);
        if (merchantBankDO != null && StringUtils.isNotEmpty(merchantBankDO.getBankNo())) {
            accountCashRespVO.setBankName(merchantBankDO.getBankName());
            accountCashRespVO.setBankNo(merchantBankDO.getBankNo());
        }
        return accountCashRespVO;
    }

    //保证金明交易明细查询
    @Override
    @SuppressWarnings("unchecked")
    public PageResult<CashDetailRespVO> list(MerchantCashReqVO merchantCashReqVO) {
        log.info("保证金明交易明细查询 req:{}", JSON.toJSONString(merchantCashReqVO));
        PageResult<MerchantCashDO> merchantCashDOPageResult = merchantCashService.queryPageByAccountNo(merchantCashReqVO);

        PageResult<CashDetailRespVO> page = new PageResult<>();
        page.setTotal(merchantCashDOPageResult.getTotal());
        List<MerchantCashDO> list = merchantCashDOPageResult.getList();
        List<CashDetailRespVO> voList = new ArrayList<>();

        if (CollectionUtil.isEmpty(list)) {
            log.info("保证金明交易明细查询-未查询到当前账户交易信息明细 accountNo:{}", merchantCashReqVO.getAccountNo());
            page.setList(voList);
            return page;
        }

        List<Long> withdrawIds = new ArrayList<>();
        //List<String> tranRecordNos = new ArrayList<>();
        for (MerchantCashDO merchantCashDO : list) {
            if (AccountConstants.TRADE_TYPE_WITHDRAW.equals(merchantCashDO.getTradeType()) || AccountConstants.TRADE_TYPE_WITHDRAWING.equals(merchantCashDO.getTradeType())) {
                //保证金提取明细ID，后续填充保证金提取流程明细
                withdrawIds.add(merchantCashDO.getId());

                if (StringUtils.isNotEmpty(merchantCashDO.getTranRecordNo())) {
                    //保证金交易流水，后续查询银行交易信息
                    //tranRecordNos.add(merchantCashDO.getTranRecordNo());
                }
            }
        }

        // 查寻提现状态记录
        Map<Long, List<PresentStatusRecordRespVO>> statusRecordMap = new HashMap<>();
        Map<String, String> transactionRecordMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(withdrawIds)) {
            statusRecordMap = merchantPresentStatusRecordMapper.selectList(new LambdaQueryWrapper<PresentStatusRecordDO>()
                    .in(PresentStatusRecordDO::getPresentNo, withdrawIds)
                    .eq(PresentStatusRecordDO::getPresentType, AccountConstants.PRESENT_TYPE_CASH)
                    .orderByAsc(PresentStatusRecordDO::getOccurredTime, PresentStatusRecordDO::getId))
                    .stream()
                    .map(PresentStatusRecordRespVO::build)
                    .collect(Collectors.groupingBy(PresentStatusRecordRespVO::getPresentNo));
        }

//            if (CollectionUtil.isNotEmpty(tranRecordNos)) {
//                transactionRecordMap = transactionRecordMapper.selectList(new LambdaQueryWrapper<TransactionRecordDO>()
//                        .in(TransactionRecordDO::getTranNo, tranRecordNos))
//                        .stream()
//                        .collect(Collectors.toMap(TransactionRecordDO::getTranNo, TransactionRecordDO::getPayeeBankAccount, (o1, o2) -> o2));
//            }

        for (MerchantCashDO merchantCashDO : list) {
            CashDetailRespVO build = CashDetailRespVO.build(merchantCashDO);
            build.setPresentStatusRecords(statusRecordMap.get(merchantCashDO.getId()));
            //build.setPayeeBankAccount(transactionRecordMap.get(merchantCashDO.getTranRecordNo()));
            //TODO:查询提现银行卡号
            build.setPayeeBankAccount("3141592654");
            voList.add(build);
        }

        page.setList(voList);
        return page;
    }

    //保证金充值
    @Override
    @Transactional
    public AccountCashRespVO recharge(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金充值 req:{}", JSON.toJSONString(transactionRecordReqVO));
        String accountNo = transactionRecordReqVO.getAccountNo();
        Long tranAmount = transactionRecordReqVO.getTranAmount();
        //增加保证金金额，版本号加1
        int count = merchantAccountService.updateCash(accountNo, tranAmount, transactionRecordReqVO.getRevision(), 1);
        if (count <= 0) {
            log.info("账户保证金充值充值失败，请求版本号错误。 Revision:{}", transactionRecordReqVO.getRevision());
            //数据修改失败
            throw new ServiceException(AccountConstants.ERROR_CODE_REVERSION_ERROR, AccountConstants.ERROR_MESSAGE_REVERSION_ERROR);
        }

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);

        boolean flag = Boolean.TRUE;
        //TODO: 调用转账接口及uctp_transaction_log日志记录
        if (!flag) {
            //转账接口调用失败
            throw new ServiceException(AccountConstants.ERROR_CODE_INTERFACE_CALL_FAILURE, AccountConstants.ERROR_MESSAGE_INTERFACE_CALL_FAILURE);
        }

        //新增保证金变动记录
        String tradeRecordNo = "" + System.currentTimeMillis();//支付流水
        merchantCashService.insertCash(merchantAccountDO, tranAmount, AccountConstants.TRADE_TYPE_RECHARGE, tradeRecordNo, null);
        return this.detail(accountNo);
    }

    //保证金提取
    @Override
    @Transactional
    public AccountCashRespVO withdraw(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金提取 req:{}", JSON.toJSONString(transactionRecordReqVO));
        String accountNo = transactionRecordReqVO.getAccountNo();
        Long tranAmount = transactionRecordReqVO.getTranAmount();
        //扣除保证金金额，增加保证金冻结金额版本号加1
        merchantAccountService.changeCash(accountNo, tranAmount, transactionRecordReqVO.getRevision(), AccountConstants.TRADE_TYPE_WITHDRAWING);

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);


        MerchantBankDO merchantBankDO = merchantBankService.getOne(
                new LambdaQueryWrapperX<MerchantBankDO>()
                        .eq(MerchantBankDO::getAccountNo, accountNo)
                        .eq(MerchantBankDO::getBusinessType, AccountEnum.BUSINESS_TYPE_CASH.getKey())
                        .eq(MerchantBankDO::getDeleted, Boolean.FALSE));

        if (merchantBankDO == null || StringUtils.isEmpty(merchantBankDO.getBankNo())) {
            throw new ServiceException(AccountConstants.ERROR_CODE_ACCOUNT_NOT_FOUND, AccountConstants.ERROR_MESSAGE_ACCOUNT_NOT_FOUND);
        }

//        TransactionRecordDO transactionRecordDO = transactionService.outGold(merchantBankDO.getBankNo(), tranAmount, AccountEnum.TRAN_TYPE_PRESENT_CASH.getKey(), transactionRecordReqVO.getContractNo());
//        if (transactionRecordDO.getTranState().equals(AccountConstants.TRAN_STATE_SUCCESS)) {
//            // 交易成功-当前调用方式为交易中
//        } else if (transactionRecordDO.getTranState().equals(AccountConstants.TRAN_STATE_DURING)){
//            //交易中
//            /**
//             * TODO::后续如何判定交易成功，交易成功后需调用
//             * {@link AccountCashServiceImpl#changeWithdrawState}
//             * 进行提现金额扣除
//             */
//            //新增保证金变动记录
//            String tradeRecordNo = "" + System.currentTimeMillis();//支付流水
//            MerchantCashDO merchantCashDO = merchantCashService.insertCash(merchantAccountDO, tranAmount, AccountConstants.TRADE_TYPE_WITHDRAWING, tradeRecordNo, null);
//
//            PresentStatusRecordDO presentStatusRecordDO = buildPresentStatusRecordDO(merchantCashDO.getId(), AccountConstants.PRESENT_STATUS_CASH_APPLY);
//            merchantPresentStatusRecordMapper.insert(presentStatusRecordDO);
//            PresentStatusRecordDO presentStatusRecordDO1 = buildPresentStatusRecordDO(merchantCashDO.getId(), AccountConstants.PRESENT_STATUS_CASH_PROCESSING);
//            merchantPresentStatusRecordMapper.insert(presentStatusRecordDO1);
//
//        } else {
//            //TODO:交易失败发起失败流程
//
//        }

        String tradeRecordNo = "" + System.currentTimeMillis();//支付流水
        MerchantCashDO merchantCashDO = merchantCashService.insertCash(merchantAccountDO, tranAmount, AccountConstants.TRADE_TYPE_WITHDRAWING, tradeRecordNo, null);

        PresentStatusRecordDO presentStatusRecordDO = buildPresentStatusRecordDO(merchantCashDO.getId(), AccountConstants.PRESENT_STATUS_CASH_APPLY);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO);
        PresentStatusRecordDO presentStatusRecordDO1 = buildPresentStatusRecordDO(merchantCashDO.getId(), AccountConstants.PRESENT_STATUS_CASH_PROCESSING);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO1);
        changeWithdrawState(tradeRecordNo);

        return this.detail(accountNo);
    }


    @Transactional
    public Boolean changeWithdrawState(String tradeRecordNo) {
        //保证金提取非实时到账 保证金提取接口withdraw 状态为保证金提取中，当前接口为保证金提取后银行返回成功，变更保证金提取状态接口

        //查询银行流水号提取金额
        MerchantCashDO merchantCash = merchantCashService.getOne(new LambdaQueryWrapperX<MerchantCashDO>()
                .eq(MerchantCashDO::getTranRecordNo, tradeRecordNo).eq(MerchantCashDO::getTradeType, AccountConstants.TRADE_TYPE_WITHDRAWING));
        if (merchantCash == null || merchantCash.getPayAmount() == null) {
            log.info("账户保证金提现实占-未查询到当前合同号冻结流水明细 contractNo:{}", tradeRecordNo);
            throw new ServiceException(AccountConstants.ERROR_CODE_CONTRACT_NO_NOT_FOUND, AccountConstants.ERROR_MESSAGE_CONTRACT_NO_NOT_FOUND);
        }

        //扣除保证金金额及保证金冻结金额版本号加1
        merchantAccountService.changeCash(merchantCash.getAccountNo(), merchantCash.getPayAmount(), null, AccountConstants.TRADE_TYPE_WITHDRAW);

        //变更保证金提取中记录为保证金提取
        merchantCashService.update(new MerchantCashDO(), new LambdaUpdateWrapper<MerchantCashDO>()
                .set(MerchantCashDO::getTradeType, AccountConstants.TRADE_TYPE_WITHDRAW)
                .eq(MerchantCashDO::getId, merchantCash.getId()));
        //新增保证金提现状态-到账成功
        PresentStatusRecordDO presentStatusRecordDO = buildPresentStatusRecordDO(merchantCash.getId(), AccountConstants.PRESENT_STATUS_CASH_SUCCESS);
        merchantPresentStatusRecordMapper.insert(presentStatusRecordDO);

        return Boolean.TRUE;
    }

    //保证金预占
    @Override
    @Transactional
    public Boolean reserve(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金预占 req:{}", JSON.toJSONString(transactionRecordReqVO));
        //保证金预占，版本号加1
        MerchantAccountDO merchantAccountDO = merchantAccountService.changeCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision(), AccountConstants.TRADE_TYPE_PREEMPTION);

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_PREEMPTION, null, transactionRecordReqVO.getContractNo());
        return Boolean.TRUE;
    }

    //保证金实占
    @Override
    @Transactional
    public Boolean deduction(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金实占 req:{}", JSON.toJSONString(transactionRecordReqVO));
        String contractNo = transactionRecordReqVO.getContractNo();
        //查询合同号预占金额
        MerchantCashDO merchantCashDO = merchantCashService.queryContractNoAmount(contractNo, Collections.singletonList(AccountConstants.TRADE_TYPE_PREEMPTION));
        if (merchantCashDO == null || merchantCashDO.getPayAmount() == null) {
            log.info("账户保证金实占-未查询到当前合同号冻结流水明细 contractNo:{}", transactionRecordReqVO.getContractNo());
            throw new ServiceException(AccountConstants.ERROR_CODE_CONTRACT_NO_NOT_FOUND, AccountConstants.ERROR_MESSAGE_CONTRACT_NO_NOT_FOUND);
        }
        //冻结金额扣除，版本号+1
        merchantAccountService.changeCash(merchantCashDO.getAccountNo(), merchantCashDO.getPayAmount(), null, AccountConstants.TRADE_TYPE_DEDUCTION);
        //更改保证金预占记录为实占
        merchantCashService.updateCashDeduction(contractNo);

        //todo：是否调用银行接口扣除金额
        return Boolean.TRUE;
    }

    //待回填保证金
    @Override
    @Transactional
    public Long difference(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金实占 req:{}", JSON.toJSONString(transactionRecordReqVO));
        //待回填保证金 = 保证金 - 冻结 - 可用；
        Long amount = 0L;
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());
        if (merchantAccountDO != null && merchantAccountDO.getCash() != null) {
            Long cash = merchantAccountDO.getCash();
            Long availableCash = 0L;
            Long freezeCash = 0L;
            if (merchantAccountDO.getAvailableCash() != null) {
                availableCash = merchantAccountDO.getAvailableCash();
            }

            if (merchantAccountDO.getFreezeCash() != null) {
                freezeCash = merchantAccountDO.getFreezeCash();
            }
            amount = cash - availableCash - freezeCash;
        }
        return amount;
    }

    //保证金回填
    @Override
    @Transactional
    public Boolean back(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金回填 req:{}", JSON.toJSONString(transactionRecordReqVO));
        String accountNo = transactionRecordReqVO.getAccountNo();
        //增加保证金金额，版本号加1
        int count = merchantAccountService.updateCash(accountNo, transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision(), 2);

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        //TODO: 是否调用转账接口

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_BACK, null, transactionRecordReqVO.getContractNo());
        return count > 0;
    }

    //保证金回填-利润
    @Override
    @Transactional
    public Boolean profitBack(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金回填-利润 req:{}", JSON.toJSONString(transactionRecordReqVO));
        //增加保证金金额，版本号加1
        int count = merchantAccountService.updateCash(transactionRecordReqVO.getAccountNo(), transactionRecordReqVO.getTranAmount(), transactionRecordReqVO.getRevision(), 3);

        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(transactionRecordReqVO.getAccountNo());
        //TODO: 是否调用转账接口

        //新增保证金变动记录
        merchantCashService.insertCash(merchantAccountDO, transactionRecordReqVO.getTranAmount(), AccountConstants.TRADE_TYPE_PROFIT_BACK, null, transactionRecordReqVO.getContractNo());
        return count > 0;
    }

    //保证金释放-冻结解冻
    @Override
    @Transactional
    public Boolean release(TransactionRecordReqVO transactionRecordReqVO) {
        log.info("账户保证金释放 req:{}", JSON.toJSONString(transactionRecordReqVO));
        //查询合同号预占金额
        MerchantCashDO merchantCashDO = merchantCashService.queryContractNoAmount(transactionRecordReqVO.getContractNo(), Collections.singletonList(AccountConstants.TRADE_TYPE_PREEMPTION));

        if (merchantCashDO == null || merchantCashDO.getPayAmount() == null) {
            log.info("账户保证金释放-未查询到当前合同号冻结流水明细 contractNo:{}", transactionRecordReqVO.getContractNo());
            throw new ServiceException(AccountConstants.ERROR_CODE_CONTRACT_NO_NOT_FOUND, AccountConstants.ERROR_MESSAGE_CONTRACT_NO_NOT_FOUND);
        }
        String accountNo = merchantCashDO.getAccountNo();
        Long payAmount = merchantCashDO.getPayAmount();
        //冻结解冻，版本号+1
        MerchantAccountDO merchantAccountDO = merchantAccountService.changeCash(accountNo, payAmount, null, AccountConstants.TRADE_TYPE_RELEASE);

        //更改保证金预占记录为实占
        merchantCashService.insertCash(merchantAccountDO, payAmount, AccountConstants.TRADE_TYPE_RELEASE, null, transactionRecordReqVO.getContractNo());
        return Boolean.TRUE;
    }

    //商户银行信息查询
    @Override
    public MerchantBankRespVO bankInfo(String accountNo) {
        log.info("商户银行信息查询 accountNo:{}", accountNo);
        MerchantAccountDO merchantAccountDO = merchantAccountService.queryByAccountNo(accountNo);
        if (merchantAccountDO == null || StringUtils.isEmpty(merchantAccountDO.getAccountNo())) {
            log.info("商户银行信息查询-未获取到当前账户信息 accountNo:{}", accountNo);
            throw new ServiceException(AccountConstants.ERROR_CODE_ACCOUNT_NOT_FOUND, AccountConstants.ERROR_MESSAGE_ACCOUNT_NOT_FOUND);
        }

        MerchantBankDO merchantBankDO = merchantBankService.getOne(
                new LambdaQueryWrapperX<MerchantBankDO>()
                        .eq(MerchantBankDO::getAccountNo, accountNo)
                        .eq(MerchantBankDO::getBusinessType, AccountEnum.BANK_NO_PROFIT.getKey())
                        .eq(MerchantBankDO::getDeleted, Boolean.FALSE));

        MerchantBankRespVO merchantBankRespVO = new MerchantBankRespVO();
        if (merchantBankDO != null && StringUtils.isNotEmpty(merchantBankDO.getBankNo())) {
            StringBuffer stringBuffer = new StringBuffer();
            int length = merchantBankDO.getBankNo().length();
            stringBuffer.append("****");
            stringBuffer.append(merchantBankDO.getBankNo().substring(length - 4, length));
            merchantBankRespVO.setBankNo(stringBuffer.toString());
            merchantBankRespVO.setBankName(merchantBankDO.getBankName());
            merchantBankRespVO.setAccountNo(accountNo);
        }
        return merchantBankRespVO;
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
}
