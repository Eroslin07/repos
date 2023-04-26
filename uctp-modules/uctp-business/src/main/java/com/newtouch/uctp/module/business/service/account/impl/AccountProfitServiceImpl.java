package com.newtouch.uctp.module.business.service.account.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.security.config.SecurityProperties;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantCashBackDO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitInvoiceDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantCashBackMapper;
import com.newtouch.uctp.module.business.dal.mysql.MerchantPresentStatusRecordMapper;
import com.newtouch.uctp.module.business.dal.mysql.MerchantProfitInvoiceMapper;
import com.newtouch.uctp.module.business.dal.mysql.MerchantProfitMapper;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.service.AccountCashService;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.account.BpmService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.account.ProfitPressentAuditOpinion;
import com.newtouch.uctp.module.business.service.account.dto.*;
import com.newtouch.uctp.module.business.service.account.event.ProfitPressentStatusChangeEvent;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;
import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;
import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.AccountConstants.PRESENT_TYPE_PROFIT;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class AccountProfitServiceImpl implements AccountProfitService {

    // 利润划入的锁key
    public static final String LOCK_PREFIX = "UCTP:ACCOUNT:PROFIT:RECORDED:";

    @Resource
    private MerchantAccountService merchantAccountService;

    @Resource
    private MerchantBankService merchantBankService;

    @Resource
    private AccountCashService accountCashService;

    @Resource
    private MerchantProfitMapper merchantProfitMapper;

    @Resource
    private MerchantProfitInvoiceMapper merchantProfitInvoiceMapper;

    @Resource
    private MerchantCashBackMapper merchantCashBackMapper;

    @Resource
    private MerchantPresentStatusRecordMapper merchantPresentStatusRecordMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private HttpServletRequest request;

    @Resource
    private BpmService bpmService;

    @Resource
    private SecurityProperties securityProperties;

    @Override
    @Transactional
    @TenantIgnore
    public List<MerchantProfitDO> recorded(String accountNo,
                                           String contractNo,
                                           Long vehicleReceiptAmount,
                                           Long carSalesAmount,
                                           List<CostDTO> costs,
                                           List<TaxDTO> taxes) {
        log.info("调用利润划入接口，accountNo:{},contractNo:{},vehicleReceiptAmount:{},carSalesAmount:{}",accountNo,contractNo,vehicleReceiptAmount,carSalesAmount);

        // 参数校验
        this.recordedCheck(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount);
        // 锁定当前合同，防止同一合同多次划入利润
        RLock lock = redissonClient.getLock(LOCK_PREFIX + contractNo);
        try {
            // 等待1s，由看门狗控制超时
            boolean locked = lock.tryLock(1, TimeUnit.SECONDS);
            if (!locked) {
                // 锁定失败，则表示重复划入
                throw exception(ACC_PRESENT_PROFIT_RECORDED_REPEAT);
            }

            // 先查询当前合同在数据库中是否已存在利润数据，存在则表示是重复划入
            LambdaQueryWrapper<MerchantProfitDO> countQueryWrapper = new LambdaQueryWrapper<>();
            countQueryWrapper.eq(MerchantProfitDO::getAccountNo, accountNo);
            countQueryWrapper.eq(MerchantProfitDO::getContractNo, contractNo);
            Long existsCurrentContractNo = this.merchantProfitMapper.selectCount(countQueryWrapper);
            if (existsCurrentContractNo > 0) {
                throw exception(ACC_PRESENT_PROFIT_RECORDED_REPEAT);
            }

            // 商户账户
            MerchantAccountDO merchantAccount = this.merchantAccountService.queryByAccountNo(accountNo);
            if (merchantAccount == null) {
                throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
            }

            // 查询待回填保证金总额
            TransactionRecordReqVO originalWaitForBackCashReq = new TransactionRecordReqVO();
            originalWaitForBackCashReq.setAccountNo(accountNo);
            Long originalWaitForBackCashTotalAmount = accountCashService.difference(originalWaitForBackCashReq);
            if (originalWaitForBackCashTotalAmount == null) {
                originalWaitForBackCashTotalAmount = 0L;
            }
            log.info("合同{}，待回填保证金总额{}", contractNo, originalWaitForBackCashTotalAmount);
            Long originalProfitTotalAmount = merchantAccount.getProfit();
            if (originalProfitTotalAmount == null) {
                originalProfitTotalAmount = 0L;
            }

            // 计算本次利润
            ProfitCalcResultDTO profitCalcResult = this.calcProfit(accountNo,
                    vehicleReceiptAmount,
                    carSalesAmount,
                    originalWaitForBackCashTotalAmount,
                    originalProfitTotalAmount,
                    costs,
                    taxes);

            log.info("合同：{}利润计算结果：{}", contractNo, profitCalcResult);
            if (profitCalcResult.getCurrentProfitAmount().compareTo(0L) <= 0) {
                // 如果本次合同卖车款小于或等于本次总费用（理论上不会出现，但要防止只能报异常处理）
                log.error("合同：{}卖车款小于或等于总费用", contractNo);
                throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
            }

            // 组装利润明细表记录
            List<MerchantProfitDO> profitList = this.buildProfitList(accountNo, contractNo, profitCalcResult);

            // 批量插入利润表
            this.merchantProfitMapper.insertBatch(profitList);
            log.info("合同：{}，利润记录至数据库", contractNo);

            // 待写入的提现状态记录
            List<PresentStatusRecordDO> statusRecordList = new ArrayList<>();
            // 回填保证金的提现利润清单
            List<MerchantProfitDO> backCashList = new ArrayList<>();
            // 需要立即付款的费用清单
            List<MerchantProfitDO> costWaitForPayList = new ArrayList<>();

            for (MerchantProfitDO mp : profitList) {
                List<PresentStatusRecordDO> psrs = mp.getPresentStatusRecords();
                if (psrs != null && !psrs.isEmpty()) {
                    for (PresentStatusRecordDO psr : psrs) {
                        psr.setPresentNo(mp.getId());
                        statusRecordList.add(psr);
                    }
                }

                if (AccountEnum.TRAN_PROFIT_CASH_BACK.getKey().equals(mp.getTradeType()) ||
                        AccountEnum.TRAN_PROFIT_CASH_DEDUCTION.getKey().equals(mp.getTradeType()) ||
                        AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getKey().equals(mp.getTradeType())) {
                    // 保证金回填交易
                    backCashList.add(mp);
                } else if (AccountEnum.TRAN_PROFIT_SERVICE_COST.getKey().equals(mp.getTradeType())
                        && StringUtils.isNotBlank(mp.getBankNo())
                        && StringUtils.isNotBlank(mp.getBankName())) {
                    // 需要立即支付的费用
                    costWaitForPayList.add(mp);
                }
            }

            if (!statusRecordList.isEmpty()) {
                // 批量插入提现状态表
                this.merchantPresentStatusRecordMapper.insertBatch(statusRecordList);
                log.info("合同：{}，提现状态表记录至数据库", contractNo);
            }

            // 组装待回填明细记录
            List<MerchantCashBackDO> waitForCashBackList = this.buildWaitForCashBackList(accountNo, contractNo, profitCalcResult);
            if (waitForCashBackList != null && !waitForCashBackList.isEmpty()) {
                this.merchantCashBackMapper.insertBatch(waitForCashBackList);
            }

            // 更新利润余额
            merchantAccount.setProfit(profitCalcResult.getNowProfitTotalAmount());
            int rows = this.merchantAccountService.updateByLock(merchantAccount);
            if (rows != 1) {
                log.error("合同：{}，利润划入时账户发生变更，利润划入失败", contractNo);
                throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
            }

            // 回填保证金
            this.cashBack(accountNo, contractNo, merchantAccount.getRevision(), profitCalcResult);

            // 处理利润回填保证金提现状态（含使用利润抵扣交易）
            for (int i = 0; i < backCashList.size(); i++) {
                MerchantProfitDO mp = backCashList.get(i);
                this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.CASH_BACK_DONE);
            }

            // 处理费用立即支付
            for (MerchantProfitDO mp : costWaitForPayList) {
                this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.COST_PAY);
                // 调用支付接口
                this.pay(mp.getBankName(), mp.getBankNo(), mp.getProfit() * -1);
                // TODO 根据支付结果处理利润余额和利润冻结
            }

            return profitList;
        } catch (InterruptedException e) {
            log.error("获取利润划入锁错误", e);
            Thread.currentThread().interrupt();
            throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
        } finally {
            // 被当前线程锁定，则需要解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @TenantIgnore
    @Transactional
    public ProfitSummaryRespVO summary(String accountNo) {
        MerchantAccountDO account = merchantAccountService.queryByAccountNo(accountNo);
        TransactionRecordReqVO waitForBackCashReq = new TransactionRecordReqVO();
        waitForBackCashReq.setAccountNo(accountNo);

        Long waitForBackCashAmount = accountCashService.difference(waitForBackCashReq);
        ProfitSummaryRespVO respVO = new ProfitSummaryRespVO();
        respVO.setProfit(account.getProfit());
        respVO.setFreezeProfit(account.getFreezeProfit());
        respVO.setCashBack(waitForBackCashAmount);

        return respVO;
    }

    @Override
    @GlobalTransactional
    @Transactional
    @TenantIgnore
    public Long profitPresent(String accountNo, Long merchantBankId, Long amount, List<ProfitPresentInvoiceReqVO> invoiceFiles) {
        log.info("调用利润提现接口，accountNo:{},merchantBankId:{},amount:{}", accountNo, merchantBankId, amount);

        // 根据银行卡ID查询账户
        MerchantBankDO bank = merchantBankService.getById(merchantBankId);
        // 银行卡必须存在，且是当前账户的银行卡
        if (bank == null || StringUtils.isBlank(bank.getBankName()) || StringUtils.isBlank(bank.getBankNo()) || !accountNo.equals(bank.getAccountNo())) {
            log.error("账户{}和银行卡{}不匹配", accountNo);
            throw exception(ACC_PRESENT_ERROR);
        }

        // 查询账户
        MerchantAccountDO account = merchantAccountService.queryByAccountNo(accountNo);
        if (account == null) {
            throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
        }

        Long profit = account.getProfit() == null ? 0L : account.getProfit(); // 余额
        Long freezeProfit = account.getFreezeProfit() == null ? 0L : account.getFreezeProfit(); // 冻结的余额
        // 判断利润余额是否足够
        if (profit.compareTo(amount) < 0) {
            throw exception(ACC_PRESENT_PROFIT_INSUFFICIENT);
        }
        account.setProfit(profit - amount); // 从余额中扣除提现额度
        account.setFreezeProfit(freezeProfit + amount); // 增加冻结额度

        // 加锁更新
        int rows = merchantAccountService.updateByLock(account);
        if (rows != 1) {
            log.error("账户：{}，利润提现时账户发生变更，利润提现失败", account);
            throw exception(ACC_PRESENT_ERROR);
        }

        LocalDateTime now = LocalDateTime.now();
        MerchantProfitDO mp = MerchantProfitDO.builder()
                .tradeType(AccountEnum.TRAN_PROFIT_PRESENT.getKey())
                .tradeTypeText(AccountEnum.TRAN_PROFIT_PRESENT.getValue())
                .tradeTo(AccountEnum.TRADE_TO_MY_PROFIT.getKey())
                .tradeToText(AccountEnum.TRADE_TO_MY_PROFIT.getValue())
                .accountNo(accountNo)
                .profit(amount * -1L)
                .presentState(null) // 提现记录初始写入状态为null
                .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记为支付
                .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                .bankName(bank.getBankName())
                .bankNo(bank.getBankNo())
                .tradeTime(now)
                .build();
        mp.setDeleted(false);
        mp.setRevision(0);

        // 保存
        merchantProfitMapper.insert(mp);

        if (invoiceFiles != null && !invoiceFiles.isEmpty()) {
            List<MerchantProfitInvoiceDO> invoices = new ArrayList<>();
            for (ProfitPresentInvoiceReqVO f : invoiceFiles) {
                MerchantProfitInvoiceDO mpi = MerchantProfitInvoiceDO.builder()
                        .profitId(mp.getId())
                        .fileId(Long.valueOf(f.getFileId()))
                        .fileUrl(f.getFileUrl())
                        .uploadTime(now)
                        .build();

                mpi.setDeleted(false);

                invoices.add(mpi);
            }
            // 保存
            merchantProfitInvoiceMapper.insertBatch(invoices);
        }

        // 触发提现申请
        this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.APPLY);
        // 提现申请后，立即变为审核中
        this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.MARKET_AUDIT_PROCESSING);

        // 发起流程
        String businessKey = this.createProfitPresentProcess(accountNo, mp.getId());

        // 把流程业务ID记录到利润提现中
        mp.setBusinessKey(businessKey);
        this.merchantProfitMapper.updateById(mp);

        return mp.getId();
    }

    @Override
    @TenantIgnore
    @Transactional
    public void auditProfitPressent(String businessKey, ProfitPressentAuditOpinion auditOpinion) {
        if (businessKey == null) {
            log.error("利润提现审核参数为空");
            throw exception(ACC_PRESENT_ERROR);
        }
        if (auditOpinion == null) {
            log.error("利润提现审核参数为空");
            throw exception(ACC_PRESENT_ERROR);
        }
        log.info("提现审核，参数：{}，审核意见：{}", businessKey, auditOpinion);

        MerchantProfitDO mp = this.merchantProfitMapper.selectOne(MerchantProfitDO::getBusinessKey, businessKey);
        if (mp == null) {
            log.error("根据业务key查询利润为空");
            throw exception(ACC_PRESENT_ERROR);
        }

        // 触发事件
        this.publishProfitPressentStatusChangeEvent(mp.getId(), auditOpinion.getEvent());

        MerchantAccountDO ma = this.merchantAccountService.queryByAccountNo(mp.getAccountNo());
        if (ma == null) {
            log.error("商户账户不存在");
            throw exception(ACC_PRESENT_ERROR);
        }

        if (auditOpinion == ProfitPressentAuditOpinion.AUDIT_REJECT) {
            // 审核退回要解除冻结
            Long profit = ma.getProfit() == null ? 0L : ma.getProfit();
            Long freezeProfit = ma.getFreezeProfit() == null ? 0L : ma.getFreezeProfit();
            ma.setProfit(profit + mp.getProfit() * -1);
            ma.setFreezeProfit(freezeProfit - mp.getProfit() * -1);

            int rows = this.merchantAccountService.updateByLock(ma);
            if (rows != 1) {
                log.error("处理利润{}审核退回时，账户有变动，处理失败", businessKey);
                // 出现并发问题
                throw exception(ACC_PRESENT_ERROR);
            }
        } else if (auditOpinion == ProfitPressentAuditOpinion.AUDIT_APPROVED) {
            // 审核通过要更新冻结金额
            Long freezeProfit = ma.getFreezeProfit() == null ? 0L : ma.getFreezeProfit();
            ma.setFreezeProfit(freezeProfit - mp.getProfit() * -1);

            int rows = this.merchantAccountService.updateByLock(ma);
            if (rows != 1) {
                log.error("处理利润{}审核退回时，账户有变动，处理失败", businessKey);
                // 出现并发问题
                throw exception(ACC_PRESENT_ERROR);
            }
        }
    }

    @Override
    @TenantIgnore
    public List<ProfitCostMonthRespVO> getMonthCostByQuarter(String accountNo, String quarter) {
        log.info("查询{}第{}季度费用", accountNo, quarter);
        String[] arr = quarter.split("Q");
        int year = Integer.valueOf(arr[0]);
        int q = Integer.valueOf(arr[1]);
        int endMonth = q * 3; // 某个季度的末月
        int startMonth = endMonth - 2; // 某个季度的首月

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.YEAR, year);
        c1.set(Calendar.MONTH, startMonth - 1);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, year);
        c2.set(Calendar.MONTH, endMonth - 1);

        LocalDateTime startTime = LocalDateTimeUtil.of(DateUtil.beginOfMonth(c1.getTime()));
        LocalDateTime endTime = LocalDateTimeUtil.of(DateUtil.endOfMonth(c2.getTime()));

        List<ProfitCostMonthRespVO> result = this.merchantProfitMapper.selectMonthCosts(accountNo, startTime, endTime,
                AccountEnum.TRAN_PROFIT_SERVICE_COST.getKey(), AccountEnum.TRAN_PROFIT_TAX_COST.getKey());
        return result;
    }

    @Override
    @TenantIgnore
    public PageResult<ProfitRespVO> profitList(String accountNo, ProfitQueryReqVO query) {
        LambdaQueryWrapper<MerchantProfitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantProfitDO::getAccountNo, accountNo)
                .orderByDesc(MerchantProfitDO::getTradeTime);
        switch (query.getType()) {
            case  FREEZE_PROFIT:
                // 交易类型是利润提现，才会有冻结
                queryWrapper.eq(MerchantProfitDO::getTradeType, AccountEnum.TRAN_PROFIT_PRESENT.getKey());

                String[] presentStateArr = new String[]{
                        AccountEnum.PRESENT_PROFIT_APPLY.getKey(),
                        AccountEnum.PRESENT_PROFIT_AUDIT_PROCESSING.getKey(),
                        AccountEnum.PRESENT_PROFIT_AUDIT_APPROVED.getKey(),
                        AccountEnum.PRESENT_PROFIT_BANK_PROCESSING.getKey()
                };

                queryWrapper.in(MerchantProfitDO::getPresentState, presentStateArr);
                break;
            case  INCOME:
                queryWrapper.eq(MerchantProfitDO::getProfitLossType, AccountEnum.PROFIT_LOSS_TYPE_INCOME.getKey());
                break;
            case DISBURSEMENT:
                queryWrapper.eq(MerchantProfitDO::getProfitLossType, AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey());
                break;
        }

        PageResult<MerchantProfitDO> pr = merchantProfitMapper.selectPage(query, queryWrapper);
        List<MerchantProfitDO> profitDOList = pr.getList();
        if (profitDOList != null && !profitDOList.isEmpty()) {
            List<ProfitRespVO> profitVOList = new ArrayList<>();
            for (MerchantProfitDO pdo : profitDOList) {
                ProfitRespVO pvo = ProfitRespVO.builder()
                        .id(pdo.getId().toString())
                        .tradeDate(DateUtil.format(pdo.getTradeTime(), NORM_DATE_PATTERN))
                        .tradeType(pdo.getTradeType())
                        .tradeTypeText(pdo.getTradeTypeText())
                        .presentState(pdo.getPresentState())
                        .presentStateText(pdo.getPresentStateText())
                        .profitLossType(pdo.getProfitLossType())
                        .profitLossTypeText(pdo.getProfitLossTypeText())
                        .amount(pdo.getProfit())
                        .build();

                profitVOList.add(pvo);
            }

            PageResult<ProfitRespVO> result = new PageResult<>(profitVOList, pr.getTotal());
            return result;
        }

        return PageResult.empty();
    }

    @Override
    @TenantIgnore
    public ProfitDetailRespVO profitDetail(String accountNo, Long profitId) {
        LambdaQueryWrapper<MerchantProfitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantProfitDO::getId, profitId)
                .eq(MerchantProfitDO::getAccountNo, accountNo);

        MerchantProfitDO p = merchantProfitMapper.selectOne(queryWrapper);
        if (p == null) {
            return null;
        } else {
            ProfitDetailRespVO respVO = new ProfitDetailRespVO();
            respVO.setContractNo(p.getContractNo());
            respVO.setTradeType(p.getTradeType());
            respVO.setTradeTypeText(p.getTradeTypeText());
            respVO.setPresentState(p.getPresentState());
            respVO.setPresentStateText(p.getPresentStateText());
            respVO.setAmount(p.getProfit());
            respVO.setTradeTo(p.getTradeTo());
            respVO.setTradeToText(p.getTradeToText());
            respVO.setProfitLossType(p.getProfitLossType());
            respVO.setProfitLossTypeText(p.getProfitLossTypeText());
            respVO.setTradeDate(p.getTradeTime() == null ? "" : DateUtil.format(p.getTradeTime(), NORM_DATETIME_PATTERN));

            if (AccountEnum.TRAN_PROFIT_PRESENT.getKey().equals(p.getTradeType())) {
                // 利润提现交易才查提现状态记录清单
                LambdaQueryWrapper<PresentStatusRecordDO> presentStatusRecordWrapper = new LambdaQueryWrapper<>();
                presentStatusRecordWrapper.eq(PresentStatusRecordDO::getPresentNo, profitId)
                        .orderByAsc(PresentStatusRecordDO::getOccurredTime); // 交易状态变更时间升序

                List<PresentStatusRecordDO> presentStatusRecords = merchantPresentStatusRecordMapper.selectList(presentStatusRecordWrapper);
                if (presentStatusRecords != null && !presentStatusRecords.isEmpty()) {
                    List<PresentStatusRecordRespVO> presentStatusRespRecords = new ArrayList<>();
                    for (PresentStatusRecordDO psr : presentStatusRecords) {
                        PresentStatusRecordRespVO psrrvo = PresentStatusRecordRespVO.builder()
                                .occurredTime(DateUtil.format(psr.getOccurredTime(), NORM_DATETIME_PATTERN))
                                .status(psr.getStatus())
                                .statusText(psr.getStatusText())
                                .build();

                        presentStatusRespRecords.add(psrrvo);
                    }

                    respVO.setPresentStatusRecords(presentStatusRespRecords);
                }

                LambdaQueryWrapper<MerchantProfitInvoiceDO> invoiceWrapper = new LambdaQueryWrapper<>();
                invoiceWrapper.eq(MerchantProfitInvoiceDO::getProfitId, profitId)
                        .orderByAsc(MerchantProfitInvoiceDO::getUploadTime); // 交易状态上传时间升序

                List<MerchantProfitInvoiceDO> invoiceFiles = merchantProfitInvoiceMapper.selectList(invoiceWrapper);
                if (invoiceFiles != null && !invoiceFiles.isEmpty()) {
                    List<ProfitPresentInvoiceRespVO> invoiceRespList = new ArrayList<>();
                    for (MerchantProfitInvoiceDO mpi : invoiceFiles) {
                        ProfitPresentInvoiceRespVO ppir = new ProfitPresentInvoiceRespVO();
                        ppir.setFileId(mpi.getFileId().toString());
                        ppir.setFileUrl(mpi.getFileUrl());

                        invoiceRespList.add(ppir);
                    }

                    respVO.setInvoiceFiles(invoiceRespList);
                }
            }

            return respVO;
        }
    }

    /**
     * 触发事件
     * @param id 利润ID
     * @param event 事件
     */
    private void publishProfitPressentStatusChangeEvent(Long id, ProfitPressentStatusChangeEvent event) {
        if (event != null) {
            log.info("利润【{}】触发【{}】事件", id, ProfitPressentStatusChangeEvent.CASH_BACK_DONE);

            MerchantProfitDO profitDO = new MerchantProfitDO();
            profitDO.setId(id);
            LambdaUpdateWrapper<MerchantProfitDO> profitDOUpdateWrapper = new LambdaUpdateWrapper<>();
            profitDOUpdateWrapper.eq(MerchantProfitDO::getId, id);
            if (event.getSourceStatus() != null) {
                profitDOUpdateWrapper.eq(MerchantProfitDO::getPresentState, event.getSourceStatus().getKey());
            } else {
                profitDOUpdateWrapper.isNull(MerchantProfitDO::getPresentState);
            }

            profitDO.setPresentState(event.getTargetStatus().getKey());
            profitDO.setPresentStateText(event.getTargetStatus().getValue());

            PresentStatusRecordDO presentStatusRecordDO = new PresentStatusRecordDO();
            presentStatusRecordDO.setPresentNo(id);
            presentStatusRecordDO.setStatus(event.getTargetStatus().getKey());
            presentStatusRecordDO.setStatusText(event.getTargetStatus().getValue());
            presentStatusRecordDO.setPresentType(PRESENT_TYPE_PROFIT);
            presentStatusRecordDO.setOccurredTime(LocalDateTime.now());
            presentStatusRecordDO.setDeleted(false);

            // 更新利润提现状态
            int rows = this.merchantProfitMapper.update(profitDO, profitDOUpdateWrapper);

            // 根据状态更新成功，则执行，否则报出异常
            if (rows == 1) {
                this.merchantPresentStatusRecordMapper.insert(presentStatusRecordDO);

                if (event.getPostEvent() != null) {
                    // 存在后置事件，则再触发一个后置事件
                    this.publishProfitPressentStatusChangeEvent(id, event.getPostEvent());
                }
            } else {
                log.error("利润：{}，提现处理时账户发生变更，处理失败", id);
                throw exception(ACC_PRESENT_ERROR);
            }

            if (event == ProfitPressentStatusChangeEvent.BANK_PROCESSING) {
                // 如果触发了银行处理中事件，则需要发起支付
                MerchantProfitDO waitForPay = this.merchantProfitMapper.selectById(id);
                this.pay(waitForPay.getBankName(), waitForPay.getBankNo(), waitForPay.getProfit() * -1);
                // TODO 根据支付结果处理利润余额和利润冻结
            }
        }
    }

    /**
     * 计算利润
     * @param accountNo 账户号
     * @param vehicleReceiptAmount 收车款
     * @param carSalesAmount 卖车款
     * @param originalWaitForBackCashTotalAmount 原待回填保证金
     * @param originalProfitTotalAmount 原利润余额
     * @param costs 服务费用清单
     * @param taxes 税费清单
     * @return 利润计算结果
     */
    private ProfitCalcResultDTO calcProfit(String accountNo,
                                           Long vehicleReceiptAmount,
                                           Long carSalesAmount,
                                           Long originalWaitForBackCashTotalAmount,
                                           Long originalProfitTotalAmount,
                                           List<CostDTO> costs,
                                           List<TaxDTO> taxes) {
        // 服务费总额=各项费用之和
        Long currentCostTotalAmount = this.calcCostAmount(costs);

        // 税费总额=各项税费之和
        Long currentTaxTotalAmount = this.calcTaxAmount(carSalesAmount, taxes);

        // 总费用=服务费总额+税费总额
        Long currentFeeTotalAmount = currentCostTotalAmount + currentTaxTotalAmount;

        // 本次收益=卖车价-收车价-总费用
        Long currentRevenueAmount = carSalesAmount - vehicleReceiptAmount - currentFeeTotalAmount;

        // 本次回填保证金=IF(本次收益>0,收车款,卖车款-费用)
        Long currentBackCashAmount = currentRevenueAmount.compareTo(0L) > 0 ? vehicleReceiptAmount : carSalesAmount - currentFeeTotalAmount;

        // 本次使用本次利润抵扣原待回填保证金=IF(原待回填保证金>0,IF(本次收益>0,IF(本次收益>原待回填保证金,原待回填保证金,本次收益),0),0)
        Long useCurrentDeductionOriginalBackCashAmount = 0L;
        if (originalWaitForBackCashTotalAmount.compareTo(0L) > 0) {
            // 原来存在待回填（不可能为负），如果本次有收益，则用本次收益回填一部分
            if (currentRevenueAmount.compareTo(0L) > 0) {
                useCurrentDeductionOriginalBackCashAmount = currentRevenueAmount.compareTo(originalWaitForBackCashTotalAmount) > 0L
                        ? originalWaitForBackCashTotalAmount : currentRevenueAmount;
            }
        }

        // 本次利润=卖车款-费用
        Long currentProfitAmount = carSalesAmount - currentFeeTotalAmount;

        // 本次待回填保证金=IF(本次收益>0,0,收车款-本次回填保证金)
        Long currentWaitForBackCashAmount = currentRevenueAmount.compareTo(0L) > 0 ? 0 : vehicleReceiptAmount - currentBackCashAmount;

        /* 本次使用原利润抵扣原待回填保证金=IF(AND(原利润余额>0,原待回填保证金-本次使用本次利润抵扣金额>0),
         IF(原利润余额>原待回填保证金-本次使用本次利润抵扣金额,原待回填保证金-本次使用本次利润抵扣金额,原利润余额),0) */
        Long useOriginalDeductionOriginalBackCashAmount = 0L;
        if (originalProfitTotalAmount.compareTo(0L) > 0 && originalWaitForBackCashTotalAmount.compareTo(useCurrentDeductionOriginalBackCashAmount) > 0) {
            useOriginalDeductionOriginalBackCashAmount = originalProfitTotalAmount.compareTo(originalWaitForBackCashTotalAmount - useCurrentDeductionOriginalBackCashAmount) > 0
                    ? originalWaitForBackCashTotalAmount - useCurrentDeductionOriginalBackCashAmount : originalProfitTotalAmount;
        }

        // 本次使用原利润抵扣本次回填保证金=IF(AND(原利润余额>0,本次回填保证金<收车款),IF(原利润余额>收车款-本次回填保证金,收车款-本次回填保证金,原利润余额),0)
        Long useOriginalDeductionBackCashAmount = 0L;
        if (originalProfitTotalAmount.compareTo(0L) > 0 && currentBackCashAmount.compareTo(vehicleReceiptAmount) < 0) {
            useOriginalDeductionBackCashAmount = originalProfitTotalAmount.compareTo(vehicleReceiptAmount - currentBackCashAmount) > 0
                    ? vehicleReceiptAmount - currentBackCashAmount : originalProfitTotalAmount;
        }

        // 现待回填保证金=原待回填保证金+本次待回填保证金-使用本次利润抵扣原待回填保证金-使用原利润抵扣原待回填保证金-使用原利润抵扣本次回填保证金
        Long nowWaitForBackCashTotalAmount = originalWaitForBackCashTotalAmount + currentWaitForBackCashAmount
                - useCurrentDeductionOriginalBackCashAmount - useOriginalDeductionOriginalBackCashAmount - useOriginalDeductionBackCashAmount;

        // 本次利润余额=本次利润-本次回填保证金-本次使用本次利润抵扣原待回填保证金
        Long currentProfitBalanceAmount = currentProfitAmount - currentBackCashAmount - useCurrentDeductionOriginalBackCashAmount;

        // 现利润余额=原利润余额-使用原利润抵扣原待回填保证金-使用原利润抵扣本次回填保证金+本次利润余额
        Long nowProfitTotalAmount = originalProfitTotalAmount - useOriginalDeductionOriginalBackCashAmount
                - useOriginalDeductionBackCashAmount + currentProfitBalanceAmount;

        ProfitCalcResultDTO result = ProfitCalcResultDTO.builder()
                // 现待回填保证金
                .nowWaitForBackCashTotalAmount(nowWaitForBackCashTotalAmount)
                // 现利润余额
                .nowProfitTotalAmount(nowProfitTotalAmount)
                // 本次回填保证金（不含本次利润扣补回填保证金）
                .currentBackCashAmount(currentBackCashAmount)
                // 本次卖车款
                .currentCarSalesAmount(carSalesAmount)
                // 本次收车款
                .currentVehicleReceiptAmount(vehicleReceiptAmount)
                // 本次待回填保证金
                .currentWaitForBackCashAmount(currentWaitForBackCashAmount)
                // 使用本次利润抵扣原待回填保证金
                .useCurrentDeductionOriginalBackCashAmount(useCurrentDeductionOriginalBackCashAmount)
                // 使用原有利润抵扣本次回填保证金
                .useOriginalDeductionBackCashAmount(useOriginalDeductionBackCashAmount)
                // 使用原有利润抵扣原待回填保证金
                .useOriginalDeductionOriginalBackCashAmount(useOriginalDeductionOriginalBackCashAmount)
                // 本次利润
                .currentProfitAmount(currentProfitAmount)
                // 本次利润余额
                .currentProfitBalanceAmount(currentProfitBalanceAmount)
                // 本次收益
                .currentRevenueAmount(currentRevenueAmount)
                // 本次总费用
                .currentFeeTotalAmount(currentFeeTotalAmount)
                // 本次费用明细
                .currentCosts(costs)
                // 本次税收明细
                .currentTaxes(taxes)
                // 计算时间
                .calcTime(LocalDateTime.now())
                .build();

        return result;
    }

    /**
     * 计算税费，并返回税费总和
     * @param carSalesAmount 卖车价
     * @param taxes 税
     * @return 剩总和
     */
    private Long calcTaxAmount(Long carSalesAmount, List<TaxDTO> taxes) {
        Long taxTotalAmouont = 0L;
        if (taxes != null && !taxes.isEmpty()) {
            Set<String> taxTypeSet = new HashSet<>();
            for (TaxDTO tax : taxes) {
                log.info("税费：{}", tax);

                String taxType = tax.getType();
                if (taxTypeSet.contains(taxType)) {
                    throw exception(ACC_TAX_TYPE_REPEAT);
                }
                BigDecimal rate = tax.getRate();
                if (rate != null) {
                    // 金额为分，计算后取整数
                    Long taxAmount = rate.multiply(BigDecimal.valueOf(carSalesAmount)).longValue();
                    // 回写税收对象
                    tax.setAmount(taxAmount);
                    taxTotalAmouont = taxTotalAmouont + taxAmount;
                } else {
                    tax.setAmount(0L); // 税率为空时，税费为0
                }
            }
        }
        return taxTotalAmouont;
    }

    /**
     * 计算所有费用
     * @param costs 费用
     * @return 费用总和
     */
    private Long calcCostAmount(List<CostDTO> costs) {
        Long r = 0L;
        if (costs != null && !costs.isEmpty()) {
            Set<String> costTypeSet = new HashSet<>();
            for (CostDTO cost : costs) {
                log.info("费用：{}", cost);

                String costType = cost.getType();
                Long costAmount = cost.getAmount();

                if (costTypeSet.contains(costType)) {
                    throw exception(ACC_COST_TYPE_REPEAT);
                }

                costTypeSet.add(costType);
                if (costAmount != null) {
                    // 费用金额不为空时添加进总费用
                    r = r + costAmount;
                }
            }
        }

        return r;
    }

    /**
     * 利润划入参数校验
     * @param accountNo
     * @param contractNo
     * @param vehicleReceiptAmount
     * @param carSalesAmount
     */
    private void recordedCheck(String accountNo,
                                     String contractNo,
                               Long vehicleReceiptAmount,
                               Long carSalesAmount) {
        if (StringUtils.isBlank(accountNo)) {
            throw exception(ACC_ACCOUNT_NO_NOT_NULL);
        }
        if (StringUtils.isBlank(contractNo)) {
            throw exception(ACC_CONTRACT_NO_NOT_NULL);
        }
        if (vehicleReceiptAmount == null) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL);
        }
        if (vehicleReceiptAmount.compareTo(0L) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
        if (carSalesAmount == null) {
            throw exception(ACC_CAR_SALES_AMOUNT_NOT_NULL);
        }
        if (carSalesAmount.compareTo(0L) < 0) {
            throw exception(ACC_CAR_SALES_AMOUNT_LESS_THAN_ZERO);
        }
    }

    /**
     * 组装利润明细
     * @param accountNo
     * @param contractNo
     * @param profitCalcResult
     * @return
     */
    private List<MerchantProfitDO> buildProfitList(String accountNo, String contractNo, ProfitCalcResultDTO profitCalcResult) {
        LocalDateTime now = profitCalcResult.getCalcTime();
        List<MerchantProfitDO> profitList = new ArrayList<>();

        // 本次卖车款，不可能为负数
        if (profitCalcResult.getCurrentCarSalesAmount().compareTo(0L) > 0) {
            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_INCOME.getKey()) // 记录成收入
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_INCOME.getValue())
                    .profit(profitCalcResult.getCurrentCarSalesAmount())
                    .tradeType(AccountEnum.TRAN_PROFIT_SALES_INCOME.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_SALES_INCOME.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_PROFIT.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_PROFIT.getValue())
                    .presentState(null) // 卖车利润初始写入无提现状态
                    .profitBalance(profitCalcResult.getCurrentProfitBalanceAmount())
                    .cashBack(profitCalcResult.getCurrentWaitForBackCashAmount())
                    .vehicleReceiptAmount(profitCalcResult.getCurrentVehicleReceiptAmount())
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 本次回填保证金（利润-扣减），不可能为负数
        if (profitCalcResult.getCurrentBackCashAmount().compareTo(0L) > 0) {
            List<PresentStatusRecordDO> presentStatusRecords = new ArrayList<>();

            PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                    .presentType(PRESENT_TYPE_PROFIT)
                    .occurredTime(now)
                    .status(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey())
                    .statusText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .build();
            psr.setDeleted(false);

            presentStatusRecords.add(psr);

            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                    .profit(profitCalcResult.getCurrentBackCashAmount() * -1) // 回填保证金为支出，记录负数
                    .tradeType(AccountEnum.TRAN_PROFIT_CASH_BACK.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_CASH_BACK.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey()) // 回填保证金支向为：我的保证金
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .presentState(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey()) // 待回填保证金
                    .presentStateText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .presentStatusRecords(presentStatusRecords)
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 本次服务费用
        if (profitCalcResult.getCurrentCosts() != null && !profitCalcResult.getCurrentCosts().isEmpty()) {
            for (CostDTO cost : profitCalcResult.getCurrentCosts()) {
                boolean isPromptPayment = cost.isPromptPayment();
                AccountEnum presentState = null;
                List<PresentStatusRecordDO> presentStatusRecords = null;
                if (isPromptPayment) {
                    presentState = AccountEnum.PRESENT_PROFIT_APPLY; // 如果费用立即付款，则需要将费用的提现状态改为申请
                    if (presentStatusRecords == null) {
                        presentStatusRecords = new ArrayList<>();

                        PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                                .presentType(PRESENT_TYPE_PROFIT)
                                .occurredTime(now)
                                .status(presentState.getKey())
                                .statusText(presentState.getValue())
                                .build();
                        psr.setDeleted(false);

                        presentStatusRecords.add(psr);
                    }
                }

                MerchantProfitDO profit = MerchantProfitDO.builder()
                        .accountNo(accountNo)
                        .contractNo(contractNo)
                        .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                        .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                        .profit(cost.getAmount() * -1) // 费用为支出，记录负数
                        .tradeType(AccountEnum.TRAN_PROFIT_SERVICE_COST.getKey())
                        .tradeTypeText(AccountEnum.TRAN_PROFIT_SERVICE_COST.getValue() + "-" + cost.getType()) // 讨论决定使用”利润-服务费-“拼接传入的服务费用名称
                        .tradeTo(AccountEnum.TRADE_TO_MARKET.getKey()) // 费用去向为：市场方
                        .tradeToText(AccountEnum.TRADE_TO_MARKET.getValue())
                        .presentState(presentState == null ? null : presentState.getKey())
                        .presentStateText(presentState == null ? null : presentState.getValue())
                        .presentStatusRecords(presentStatusRecords)
                        .tradeTime(now)
                        .build();
                profit.setDeleted(false);
                profit.setRevision(0);

                if (isPromptPayment) {
                    if (StringUtils.isBlank(cost.getBankName())) {
                        throw exception(ACC_PRESENT_PROFIT_BANK_NOT_NULL);
                    }
                    if (StringUtils.isBlank(cost.getBankNo())) {
                        throw exception(ACC_PRESENT_PROFIT_BANK_NOT_NULL);
                    }
                    profit.setBankName(cost.getBankName());
                    profit.setBankNo(cost.getBankNo());
                }

                profitList.add(profit);
            }
        }

        // 本次税费
        if (profitCalcResult.getCurrentTaxes() != null && !profitCalcResult.getCurrentTaxes().isEmpty()) {
            for (TaxDTO tax : profitCalcResult.getCurrentTaxes()) {
                MerchantProfitDO profit = MerchantProfitDO.builder()
                        .accountNo(accountNo)
                        .contractNo(contractNo)
                        .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                        .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                        .profit(tax.getAmount() * -1) // 税费为支出，记录负数
                        .tradeType(AccountEnum.TRAN_PROFIT_TAX_COST.getKey())
                        .tradeTypeText(AccountEnum.TRAN_PROFIT_TAX_COST.getValue() + "-" + tax.getType())
                        .tradeTo(AccountEnum.TRADE_TO_MARKET.getKey()) // 税费去向为：市场方
                        .tradeToText(AccountEnum.TRADE_TO_MARKET.getValue())
                        .presentState(null) // 初始写入税费无提现状态
                        .tradeTime(now)
                        .build();
                profit.setDeleted(false);
                profit.setRevision(0);

                profitList.add(profit);
            }
        }

        // 本次利润
        if (profitCalcResult.getCurrentProfitAmount().compareTo(0L) > 0) {
            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_IGNORE.getKey()) // 记录成不计入（冗余记录，不算收支）
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_IGNORE.getValue())
                    .profit(profitCalcResult.getCurrentProfitAmount())
                    .tradeType(AccountEnum.TRAN_PROFIT_SALES_PROFIT.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_SALES_PROFIT.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_PROFIT.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_PROFIT.getValue())
                    .presentState(null) // 利润初始写入无提现状态
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 本次利润回填原待回填保证金
        if (profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount().compareTo(0L) > 0) {
            List<PresentStatusRecordDO> presentStatusRecords = new ArrayList<>();

            PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                    .presentType(PRESENT_TYPE_PROFIT)
                    .occurredTime(now)
                    .status(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey())
                    .statusText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .build();
            psr.setDeleted(false);

            presentStatusRecords.add(psr);

            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                    .profit(profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount() * -1) // 回填保证金为支出，记录负数
                    .tradeType(AccountEnum.TRAN_PROFIT_CASH_DEDUCTION.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_CASH_DEDUCTION.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey()) // 回填保证金支向为：我的保证金
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .presentState(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey()) // 待回填保证金
                    .presentStateText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .presentStatusRecords(presentStatusRecords)
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 原有利润抵扣本次回填保证金
        if (profitCalcResult.getUseOriginalDeductionBackCashAmount().compareTo(0L) > 0) {
            List<PresentStatusRecordDO> presentStatusRecords = new ArrayList<>();

            PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                    .presentType(PRESENT_TYPE_PROFIT)
                    .occurredTime(now)
                    .status(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey())
                    .statusText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .build();
            psr.setDeleted(false);

            presentStatusRecords.add(psr);

            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                    .profit(profitCalcResult.getUseOriginalDeductionBackCashAmount() * -1) // 回填保证金为支出，记录负数
                    .tradeType(AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey()) // 回填保证金支向为：我的保证金
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .presentState(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey()) // 待回填保证金
                    .presentStateText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .presentStatusRecords(presentStatusRecords)
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 原有利润抵扣原待回填保证金（理论上不会有该情况）
        if (profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount().compareTo(0L) > 0) {
            List<PresentStatusRecordDO> presentStatusRecords = new ArrayList<>();

            PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                    .presentType(PRESENT_TYPE_PROFIT)
                    .occurredTime(now)
                    .status(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey())
                    .statusText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .build();
            psr.setDeleted(false);

            presentStatusRecords.add(psr);

            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profitLossType(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getKey()) // 记录成支出
                    .profitLossTypeText(AccountEnum.PROFIT_LOSS_TYPE_DISBURSEMENT.getValue())
                    .profit(profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount() * -1) // 回填保证金为支出，记录负数
                    .tradeType(AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getKey())
                    .tradeTypeText(AccountEnum.TRAN_PROFIT_CASH_BACK_FROM_ORIGINAL_PROFIT.getValue())
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey()) // 回填保证金支向为：我的保证金
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .presentState(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getKey()) // 待回填保证金
                    .presentStateText(AccountEnum.PRESENT_PROFIT_CASH_BACK_WAIT.getValue())
                    .presentStatusRecords(presentStatusRecords)
                    .tradeTime(now)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        return profitList;
    }

    /**
     * 组装待回填明细
     * @param accountNo 账户
     * @param contractNo 合同号
     * @param profitCalcResult 计算结果
     * @return
     */
    private List<MerchantCashBackDO> buildWaitForCashBackList(String accountNo, String contractNo, ProfitCalcResultDTO profitCalcResult) {
        List<MerchantCashBackDO> waitForCashBackList = new ArrayList<>();
        // 本次待回填
        if (profitCalcResult.getCurrentWaitForBackCashAmount().compareTo(0L) > 0) {
            MerchantCashBackDO mcb = MerchantCashBackDO.builder()
                    .accountNo(accountNo) // 商户账户号
                    .contractNo(contractNo) // 交易合同号
                    .type(AccountEnum.CASH_BACK_TYPE_WAIT.getKey()) // 类型（如：待回填，利润抵扣）
                    .typeText(AccountEnum.CASH_BACK_TYPE_WAIT.getValue()) // 类型中文名称
                    .occurredTime(profitCalcResult.getCalcTime()) // 发生时间
                    .amount(profitCalcResult.getCurrentWaitForBackCashAmount()) // 金额
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .build();

            waitForCashBackList.add(mcb);
        }

        // 使用利润（本次利润或原利润）抵扣回填保证金
        if (profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount().compareTo(0L) > 0) {
            MerchantCashBackDO mcb = MerchantCashBackDO.builder()
                    .accountNo(accountNo) // 商户账户号
                    .contractNo(contractNo) // 交易合同号
                    .type(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getKey()) // 类型（如：待回填，利润抵扣）
                    .typeText(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getValue()) // 类型中文名称
                    .occurredTime(profitCalcResult.getCalcTime()) // 发生时间
                    .amount(profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount() * -1) // 金额
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .build();

            waitForCashBackList.add(mcb);
        }
        if (profitCalcResult.getUseOriginalDeductionBackCashAmount().compareTo(0L) > 0) {
            MerchantCashBackDO mcb = MerchantCashBackDO.builder()
                    .accountNo(accountNo) // 商户账户号
                    .contractNo(contractNo) // 交易合同号
                    .type(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getKey()) // 类型（如：待回填，利润抵扣）
                    .typeText(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getValue()) // 类型中文名称
                    .occurredTime(profitCalcResult.getCalcTime()) // 发生时间
                    .amount(profitCalcResult.getUseOriginalDeductionBackCashAmount() * -1) // 金额
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .build();

            waitForCashBackList.add(mcb);
        }
        // 理论上不存在该场景
        if (profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount().compareTo(0L) > 0) {
            MerchantCashBackDO mcb = MerchantCashBackDO.builder()
                    .accountNo(accountNo) // 商户账户号
                    .contractNo(contractNo) // 交易合同号
                    .type(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getKey()) // 类型（如：待回填，利润抵扣）
                    .typeText(AccountEnum.CASH_BACK_TYPE_PROFIT_DEDUCTION.getValue()) // 类型中文名称
                    .occurredTime(profitCalcResult.getCalcTime()) // 发生时间
                    .amount(profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount() * -1) // 金额
                    .tradeTo(AccountEnum.TRADE_TO_MY_CASH.getKey())
                    .tradeToText(AccountEnum.TRADE_TO_MY_CASH.getValue())
                    .build();

            waitForCashBackList.add(mcb);
        }

        return waitForCashBackList;
    }

    /**
     * 回填保证金
     * @param accountNo 账户
     * @param contractNo 合同号
     * @param accountRevision 账户表版本号
     * @param profitCalcResult 计算结果
     */
    private void cashBack(String accountNo, String contractNo, Integer accountRevision, ProfitCalcResultDTO profitCalcResult) {
        Integer nowAccountRevision = accountRevision;
        // 回填保证金
        if (profitCalcResult.getCurrentBackCashAmount().compareTo(0L) > 0) {
            // 回填保证金
            TransactionRecordReqVO backCash = new TransactionRecordReqVO();
            backCash.setAccountNo(accountNo);
            backCash.setContractNo(contractNo);
            backCash.setTranAmount(profitCalcResult.getCurrentBackCashAmount());
            nowAccountRevision = nowAccountRevision + 1;
            backCash.setRevision(nowAccountRevision); // 注意版本号

            log.info("合同：{}回填保证金{}}", contractNo, backCash.getTranAmount());
            boolean backSuccessed = accountCashService.back(backCash);
            if (!backSuccessed) {
                log.error("合同：{}，利润划入时账户发生变更，利润划入失败", contractNo);
                throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
            }
        }
        if (profitCalcResult.getUseOriginalDeductionBackCashAmount().compareTo(0L) > 0 ||
                profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount().compareTo(0L) > 0 ||
                profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount().compareTo(0L) > 0 ) {
            // 回填保证金（使用利润抵扣）
            TransactionRecordReqVO backCashFromProfit = new TransactionRecordReqVO();
            backCashFromProfit.setAccountNo(accountNo);
            backCashFromProfit.setContractNo(contractNo);
            backCashFromProfit.setTranAmount(profitCalcResult.getUseOriginalDeductionBackCashAmount() +
                    profitCalcResult.getUseOriginalDeductionOriginalBackCashAmount() +
                    profitCalcResult.getUseCurrentDeductionOriginalBackCashAmount());
            nowAccountRevision = nowAccountRevision + 1;
            backCashFromProfit.setRevision(nowAccountRevision); // 注意版本号

            log.info("合同：{}使用{}利润回填保证金", contractNo, backCashFromProfit.getTranAmount());
            boolean backSuccessed = accountCashService.profitBack(backCashFromProfit);
            if (!backSuccessed) {
                log.error("合同：{}，利润划入时账户发生变更，利润划入失败", contractNo);
                throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
            }
        }
    }

    /**
     * 发起提现流程
     * @param accountNo 账户
     * @param profitId 提现id
     * @return
     */
    private String createProfitPresentProcess(String accountNo, Long profitId) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> formDataJson = new HashMap<>();
        Map<String, Object> formMain = new HashMap<>();

        ProfitPresentFormDTO profitPresentFormDTO = this.merchantProfitMapper.selectProfitById(profitId);
        // 处理数据
        if (profitPresentFormDTO != null) {
            profitPresentFormDTO.setTelNo("122222");
            profitPresentFormDTO.setMerchantName("XX商户");
            profitPresentFormDTO.setBankOfDeposit("XX开户行");
            List<ProfitPresentFormDetailDTO> details = profitPresentFormDTO.getProfitDetails();
            if (details != null && !details.isEmpty()) {
                details.parallelStream().forEach(e -> {
                    e.setMerchantName(profitPresentFormDTO.getMerchantName());
                    e.setTelNo(profitPresentFormDTO.getTelNo());
                    e.setBalanceAmount(profitPresentFormDTO.getBalanceAmount());
                });
            }
        }

        formMain.put("merchantId", SecurityFrameworkUtils.getLoginUser().getTenantId());
        formMain.put("formDataJson", profitPresentFormDTO);

        formDataJson.put("formMain", formMain);

        variables.put("marketName", "");
        variables.put("merchantName", "");
        variables.put("startUserId", SecurityFrameworkUtils.getLoginUser().getId());
        variables.put("formDataJson", formDataJson);

        requestBody.put("procDefKey", "LRTX");
        requestBody.put("variables", variables);

        log.info("开始调用发起流程接口，利润提现ID: {}", profitId);
        CommonResult<String> r = bpmService.create(SecurityFrameworkUtils.getLoginUser().getTenantId(), token, requestBody);
        log.info("利润提现ID{}， 创建利润提现流程结果：{}", profitId, r);
        if (r.isError()) {
            log.error("账户：{}，利润提现流程创建失败", accountNo);
            throw exception(ACC_PRESENT_ERROR);
        }
        return r.getData();
    }

    /**
     * 支付，内部用调支付接口
     * @param bankName 银行卡账户名
     * @param bankNo 银行卡号
     * @param amount 支付金额
     */
    private void pay(String bankName, String bankNo, Long amount) {
        // TODO: 调用支付接口

    }
}
