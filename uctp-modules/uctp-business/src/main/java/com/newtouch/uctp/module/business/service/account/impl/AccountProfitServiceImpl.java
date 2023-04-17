package com.newtouch.uctp.module.business.service.account.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PresentStatusRecordRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitQueryReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.PresentStatusRecordDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantPresentStatusRecordMapper;
import com.newtouch.uctp.module.business.dal.mysql.MerchantProfitMapper;
import com.newtouch.uctp.module.business.enums.AccountConstants;
import com.newtouch.uctp.module.business.service.AccountCashService;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.account.ProfitPressentAuditOpinion;
import com.newtouch.uctp.module.business.service.account.dto.CostDTO;
import com.newtouch.uctp.module.business.service.account.dto.ProfitCalcResultDTO;
import com.newtouch.uctp.module.business.service.account.dto.TaxDTO;
import com.newtouch.uctp.module.business.service.account.event.ProfitPressentStatusChangeEvent;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATE_PATTERN;
import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.AccountConstants.*;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class AccountProfitServiceImpl implements AccountProfitService {

    @Resource
    private MerchantAccountService merchantAccountService;

    @Resource
    private MerchantBankService merchantBankService;

    @Resource
    private AccountCashService accountCashService;

    @Resource
    private MerchantProfitMapper merchantProfitMapper;

    @Resource
    private MerchantPresentStatusRecordMapper merchantPresentStatusRecordMapper;

    @Override
    @Transactional
    @TenantIgnore
    public List<MerchantProfitDO> recorded(String accountNo,
                                           String contractNo,
                                           Integer vehicleReceiptAmount,
                                           Integer carSalesAmount,
                                           List<CostDTO> costs,
                                           List<TaxDTO> taxes) {
        log.info("调用利润划入接口，accountNo:{},contractNo:{},vehicleReceiptAmount:{},carSalesAmount:{}",accountNo,contractNo,vehicleReceiptAmount,carSalesAmount);
        // 参数校验
        this.recordedCheck(accountNo, contractNo, vehicleReceiptAmount, carSalesAmount);
        // 商户账户
        MerchantAccountDO merchantAccount = this.merchantAccountService.queryByAccountNo(accountNo);
        if (merchantAccount == null) {
            throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
        }
        Integer originalWaitForBackCashTotalAmount = 0; // 账户表暂无待回填保证金 todo
        if (originalWaitForBackCashTotalAmount == null) {
            originalWaitForBackCashTotalAmount = 0;
        }
        Integer originalProfitTotalAmount = merchantAccount.getProfit();
        if (originalProfitTotalAmount == null) {
            originalProfitTotalAmount = 0;
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

            if (AccountConstants.TRAN_PROFIT_CASH_BACK.equals(mp.getTradeType())) {
                // 保证金回填交易
                backCashList.add(mp);
            } else if (AccountConstants.TRAN_PROFIT_SERVICE_COST.equals(mp.getTradeType())
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

        // 更新利润余额
        merchantAccount.setProfit(profitCalcResult.getProfitTotalAmount());
        int rows = this.merchantAccountService.updateByLock(merchantAccount);
        if (rows != 1) {
            log.error("合同：{}，利润划入时账户发生变更，利润划入失败", contractNo);
            throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
        }

        // 处理利润回填保证金提现状态
        for (int i = 0; i < backCashList.size(); i++) {
            MerchantProfitDO mp = backCashList.get(i);
            this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.CASH_BACK_DONE);

            if (profitCalcResult.getRevenueAmount().compareTo(0) < 0 && profitCalcResult.getDeductionBackCashFromOriginalProfitAmount().compareTo(0) > 0) {
                // 本次收益为负，并且使用了原有利润回填保证金，则需要分别调用回填保证金和使用原利润回填保证金接口
                // 回填保证金
                TransactionRecordReqVO backCash = new TransactionRecordReqVO();
                backCash.setAccountNo(mp.getAccountNo());
                backCash.setContractNo(mp.getContractNo());
                backCash.setTranAmount((mp.getProfit() - profitCalcResult.getDeductionBackCashFromOriginalProfitAmount()) * -1); // 本次实际回填总额-占用原有的利润额度
                backCash.setRevision(merchantAccount.getRevision() + 1); // 注意版本号

                log.info("合同：{}回填保证金{}}", contractNo, backCash.getTranAmount());
                boolean backSuccessed = accountCashService.back(backCash);
                if (!backSuccessed) {
                    throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
                }

                // 回填保证金（使用原有利润）
                TransactionRecordReqVO backCashFromOriginalProfit = new TransactionRecordReqVO();
                backCashFromOriginalProfit.setAccountNo(mp.getAccountNo());
                backCashFromOriginalProfit.setContractNo(mp.getContractNo());
                backCashFromOriginalProfit.setTranAmount(profitCalcResult.getDeductionBackCashFromOriginalProfitAmount() * -1); // 占用的原有利润
                backCashFromOriginalProfit.setRevision(backCash.getRevision() + 1); // 注意版本号

                log.info("合同：{}使用{}原有利润回填保证金", contractNo);
                boolean profitBackSuccessed = accountCashService.profitBack(backCashFromOriginalProfit);
                if (!profitBackSuccessed) {
                    throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
                }
            } else {
                // 不使用原有利润回填保证金
                // 回填保证金
                TransactionRecordReqVO backCash = new TransactionRecordReqVO();
                backCash.setAccountNo(mp.getAccountNo());
                backCash.setContractNo(mp.getContractNo());
                backCash.setTranAmount(mp.getProfit() * -1);
                backCash.setRevision(merchantAccount.getRevision() + 1); // 注意版本号

                log.info("合同：{}回填保证金{}}", contractNo, backCash.getTranAmount());
                boolean backSuccessed = accountCashService.back(backCash);

                if (!backSuccessed) {
                    throw exception(ACC_PRESENT_PROFIT_RECORDED_ERROR);
                }
            }

        }

        // 处理费用立即支付
        for (MerchantProfitDO mp : costWaitForPayList) {
            this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.COST_PAY);
            // 调用支付接口
            this.pay(mp.getBankName(), mp.getBankNo(), mp.getProfit() * -1);
            // TODO 根据支付结果处理利润余额和利润冻结
        }

        return profitList;
    }

    @Override
    @TenantIgnore
    public MerchantAccountDO queryByAccountNo(String accountNo) {
        return merchantAccountService.queryByAccountNo(accountNo);
    }

    @Override
    @Transactional
    @TenantIgnore
    public Long profitPresent(String accountNo, Long merchantBankId, Integer amount, List<String> invoiceIds) {
        String invoiceIdsStr = null;
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            invoiceIdsStr = StringUtils.join(invoiceIds, "#");
        }
        log.info("调用利润提现接口，accountNo:{},merchantBankId:{},amount:{},invoiceIds:{}", accountNo, merchantBankId, amount, invoiceIdsStr);

        // 根据银行卡ID查询账户
        MerchantBankDO bank = merchantBankService.getById(merchantBankId);
        if (bank == null || StringUtils.isBlank(bank.getBankName()) || StringUtils.isBlank(bank.getBankNo())) {
            throw exception(ACC_PRESENT_ERROR);
        }

        // 查询账户
        MerchantAccountDO account = merchantAccountService.queryByAccountNo(accountNo);
        if (account == null) {
            throw exception(ACC_PRESENT_ERROR);
        }

        Integer profit = account.getProfit() == null ? 0 : account.getProfit(); // 余额
        Integer freezeProfit = account.getFreezeProfit() == null ? 0 : account.getFreezeProfit(); // 冻结的余额
        // 判断利润余额是否足够
        if (profit.compareTo(amount) < 0) {
            throw exception(ACC_PRESENT_PROFIT_INSUFFICIENT);
        }
        account.setProfit(profit - amount); // 从余额中扣除提现额度
        account.setFreezeProfit(freezeProfit + amount); // 增加冻结额度

        // 加锁更新
        int rows = merchantAccountService.updateByLock(account);
        if (rows != 1) {
            throw exception(ACC_PRESENT_ERROR);
        }

        MerchantProfitDO mp = MerchantProfitDO.builder()
                .tradeType(AccountConstants.TRAN_PROFIT_PRESENT)
                .tradeTo(AccountConstants.TRADE_TO_MY_PROFIT)
                .accountNo(accountNo)
                .profit(amount * -1)
                .presentState(null) // 提现记录初始写入状态为null
                .bankName(bank.getBankName())
                .bankNo(bank.getBankNo())
                .build();
        mp.setDeleted(false);
        mp.setRevision(0);

        // 保存
        merchantProfitMapper.insert(mp);
        // 触发提现申请
        this.publishProfitPressentStatusChangeEvent(mp.getId(), ProfitPressentStatusChangeEvent.APPLY);

        return mp.getId();
    }

    @Override
    @TenantIgnore
    @Transactional
    public void auditProfitPressent(Long id, ProfitPressentAuditOpinion auditOpinion) {
        if (id == null) {
            throw exception(ACC_PRESENT_ERROR);
        }
        if (auditOpinion == null) {
            throw exception(ACC_PRESENT_ERROR);
        }
        log.info("提现审核，参数：{}，审核意见：{}", id, auditOpinion);

        // 触发事件
        this.publishProfitPressentStatusChangeEvent(id, auditOpinion.getEvent());

        // 审核退回
        if (auditOpinion == ProfitPressentAuditOpinion.AUDIT_REJECT) {
            // 审核退回要解除冻结
            MerchantProfitDO mp = this.merchantProfitMapper.selectById(id);
            if (mp != null) {
                MerchantAccountDO ma = this.merchantAccountService.queryByAccountNo(mp.getAccountNo());
                if (ma != null) {
                    Integer profit = ma.getProfit() == null ? 0 : ma.getProfit();
                    Integer freezeProfit = ma.getFreezeProfit() == null ? 0 : ma.getFreezeProfit();
                    ma.setProfit(profit + mp.getProfit() * -1);
                    ma.setFreezeProfit(freezeProfit - mp.getProfit() * -1);

                    int rows = this.merchantAccountService.updateByLock(ma);
                    if (rows != 1) {
                        log.error("处理利润{}审核退回时，账户有变动，处理失败", id);
                        // 出现并发问题
                        throw exception(ACC_PRESENT_ERROR);
                    }
                }
            }
        }
    }

    @Override
    @TenantIgnore
    public PageResult<ProfitRespVO> profitList(String accountNo, ProfitQueryReqVO query) {
        LambdaQueryWrapper<MerchantProfitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MerchantProfitDO::getAccountNo, accountNo)
                .orderByDesc(MerchantProfitDO::getCreateTime);

        PageResult<MerchantProfitDO> pr = merchantProfitMapper.selectPage(query, queryWrapper);
        List<MerchantProfitDO> profitDOList = pr.getList();
        if (profitDOList != null && !profitDOList.isEmpty()) {
            List<ProfitRespVO> profitVOList = new ArrayList<>();
            for (MerchantProfitDO pdo : profitDOList) {
                ProfitRespVO pvo = ProfitRespVO.builder()
                        .profitLossType(null) // 损益类型
                        .tradeDate(null)
                        .tradeType(pdo.getTradeType())
                        .presentState(pdo.getPresentState())
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
            respVO.setPresentState(p.getPresentState());
            respVO.setAmount(p.getProfit());
            respVO.setTradeTo(p.getTradeTo());
            respVO.setProfitLossType(null); // 暂无 todo 待补充
            respVO.setTradeDate(null); // 暂无 todo 待补充

            // 查一下提现状态记录清单
            LambdaQueryWrapper<PresentStatusRecordDO> presentStatusRecordWrapper = new LambdaQueryWrapper<>();
            presentStatusRecordWrapper.eq(PresentStatusRecordDO::getPresentNo, profitId)
                            .orderByAsc(PresentStatusRecordDO::getOccurredTime); // 交易状态变更时间升序

            List<PresentStatusRecordDO> presentStatusRecords = merchantPresentStatusRecordMapper.selectList(presentStatusRecordWrapper);
            if (presentStatusRecords != null && !presentStatusRecords.isEmpty()) {
                List<PresentStatusRecordRespVO> presentStatusRespRecords = new ArrayList<>();
                for (PresentStatusRecordDO psr : presentStatusRecords) {
                    PresentStatusRecordRespVO psrrvo = PresentStatusRecordRespVO.builder()
                            .occurredTime(DateUtil.format(psr.getOccurredTime(), NORM_DATE_PATTERN))
                            .status(psr.getStatus())
                            .build();

                    presentStatusRespRecords.add(psrrvo);
                }

                respVO.setPresentStatusRecords(presentStatusRespRecords);
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
                profitDOUpdateWrapper.eq(MerchantProfitDO::getPresentState, event.getSourceStatus());
            } else {
                profitDOUpdateWrapper.isNull(MerchantProfitDO::getPresentState);
            }

            profitDO.setPresentState(event.getTargetStatus());

            PresentStatusRecordDO presentStatusRecordDO = new PresentStatusRecordDO();
            presentStatusRecordDO.setPresentNo(id);
            presentStatusRecordDO.setStatus(event.getTargetStatus());
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
     * @param vehicleReceiptAmount 收车价
     * @param carSalesAmount 卖车价
     * @param originalWaitForBackCashTotalAmount 原待回填保证金
     * @param originalProfitTotalAmount 原利润余额
     * @param costs 服务费用清单
     * @param taxes 税费清单
     * @return 利润计算结果
     */
    private ProfitCalcResultDTO calcProfit(String accountNo,
                                           Integer vehicleReceiptAmount,
                                           Integer carSalesAmount,
                                           Integer originalWaitForBackCashTotalAmount,
                                           Integer originalProfitTotalAmount,
                                           List<CostDTO> costs,
                                           List<TaxDTO> taxes) {
        // 服务费总额=各项费用之和
        Integer costTotalAmount = this.calcCostAmount(costs);

        // 税费总额=各项税费之和
        Integer taxTotalAmount = this.calcTaxAmount(carSalesAmount, taxes);

        // 总费用=服务费总额+税费总额
        Integer tmpFeeTotalAmount = costTotalAmount + taxTotalAmount;

        // 本次收益=卖车价-收车价-总费用
        Integer tmpTheRevenueAmount = carSalesAmount - vehicleReceiptAmount - tmpFeeTotalAmount;

        // 本次可用回填保证金=原利润余额+卖车价-总费用
        Integer tmpTheAvailableBackCashAmount = originalProfitTotalAmount + carSalesAmount - tmpFeeTotalAmount;

        // 本次应回填保证金合计=原待回填保证金+收车价
        Integer tmpTheShouldBackCashTotalAmount = originalWaitForBackCashTotalAmount + vehicleReceiptAmount;

        // 本次实回填保证金=IF(本次应回填保证金合计<=本次可用回填保证金,本次应回填保证金合计,本次可用回填保证金)
        Integer tmpTheActualBackCashAmount = tmpTheShouldBackCashTotalAmount.compareTo(tmpTheAvailableBackCashAmount) <= 0 ?
                tmpTheShouldBackCashTotalAmount : tmpTheAvailableBackCashAmount;

        // 本次应回填保证金=收车价
        Integer tmpTheShouldBackCashAmount = vehicleReceiptAmount;

        // 本次待回填保证金=IF(本次收益>0,0,本次应回填保证金-本次实回填保证金)
        Integer theWaitForBackCashAmount = tmpTheRevenueAmount.compareTo(0) > 0 ? 0 : tmpTheShouldBackCashAmount - tmpTheActualBackCashAmount;

        // 本次回填保证金=本次实回填保证金
        Integer theBackCashAmount = tmpTheActualBackCashAmount;

        // 本次卖车利润=卖车价
        Integer theProfitAmount = carSalesAmount;

        // 本次扣补回填保证金=IF(本次实回填保证金>本次应回填保证金,本次实回填保证金-本次应回填保证金,0)
        Integer theDeductionBackCashAmount = tmpTheActualBackCashAmount.compareTo(tmpTheShouldBackCashTotalAmount) >0 ?
                tmpTheActualBackCashAmount - tmpTheShouldBackCashTotalAmount : 0;

        // 现待回填保证金=本次待回填保证金+原待回填保证金-本次扣补回填保证金
        Integer nowWaitForBackCashTotalAmount = theWaitForBackCashAmount + originalWaitForBackCashTotalAmount - theDeductionBackCashAmount;

        // 现利润余额=原利润余额+本次收益-(本次实回填保证金-收车价)
        Integer nowProfitTotalAmount = originalProfitTotalAmount + tmpTheRevenueAmount - (tmpTheActualBackCashAmount - vehicleReceiptAmount);

        // 本次使用原有利润金额=IF(原利润余额-现利润余额>0,原利润余额-现利润余额,0)
        Integer theDeductionBackCashFromOriginalProfitAmount = originalProfitTotalAmount - nowProfitTotalAmount > 0 ? originalProfitTotalAmount - nowProfitTotalAmount : 0;

        ProfitCalcResultDTO result = ProfitCalcResultDTO.builder()
                // 本次回填保证金
                .backCashAmount(theBackCashAmount)
                // 本次卖车利润
                .profitAmount(theProfitAmount)
                // 本次扣减补保证金
                .deductionBackCashAmount(theDeductionBackCashAmount)
                // 本次使用原有利润金额
                .deductionBackCashFromOriginalProfitAmount(theDeductionBackCashFromOriginalProfitAmount)
                // 本次待回填保证金
                .waitForBackCashAmount(theWaitForBackCashAmount)
                // 现利润总额
                .profitTotalAmount(nowProfitTotalAmount)
                // 现待回填保证金总额
                .waitForBackCashTotalAmount(nowWaitForBackCashTotalAmount)
                // 本次收益
                .revenueAmount(tmpTheRevenueAmount)
                // 本次税收
                .taxes(taxes)
                // 本次费用
                .costs(costs)
                .build();

        return result;
    }

    /**
     * 计算税费，并返回税费总和
     * @param carSalesAmount 卖车价
     * @param taxes 税
     * @return 剩总和
     */
    private Integer calcTaxAmount(Integer carSalesAmount, List<TaxDTO> taxes) {
        Integer taxTotalAmouont = 0;
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
                    Integer taxAmount = rate.multiply(BigDecimal.valueOf(carSalesAmount)).intValue();
                    // 回写税收对象
                    tax.setAmount(taxAmount);
                    taxTotalAmouont = taxTotalAmouont + taxAmount;
                } else {
                    tax.setAmount(0); // 税率为空时，税费为0
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
    private Integer calcCostAmount(List<CostDTO> costs) {
        Integer r = 0;
        if (costs != null && !costs.isEmpty()) {
            Set<String> costTypeSet = new HashSet<>();
            for (CostDTO cost : costs) {
                log.info("费用：{}", cost);

                String costType = cost.getType();
                Integer costAmount = cost.getAmount();

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
                                     Integer vehicleReceiptAmount,
                                     Integer carSalesAmount) {
        if (StringUtils.isBlank(accountNo)) {
            throw exception(ACC_ACCOUNT_NO_NOT_NULL);
        }
        if (StringUtils.isBlank(contractNo)) {
            throw exception(ACC_CONTRACT_NO_NOT_NULL);
        }
        if (vehicleReceiptAmount == null) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_NOT_NULL);
        }
        if (vehicleReceiptAmount.compareTo(0) < 0) {
            throw exception(ACC_VEHICLE_RECEIPT_AMOUNT_LESS_THAN_ZERO);
        }
        if (carSalesAmount == null) {
            throw exception(ACC_CAR_SALES_AMOUNT_NOT_NULL);
        }
        if (carSalesAmount.compareTo(0) < 0) {
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
        LocalDateTime now = LocalDateTime.now();
        List<MerchantProfitDO> profitList = new ArrayList<>();

        // 本次卖车利润，不可能为负数
        if (profitCalcResult.getProfitAmount().compareTo(0) > 0) {
            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profit(profitCalcResult.getProfitAmount())
                    .tradeType(TRAN_PROFIT_SALES_PROFIT)
                    .tradeTo(TRADE_TO_MY_PROFIT)
                    .presentState(null) // 卖车利润初始写入无提现状态
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 本次回填保证金，不可能为负数
        if (profitCalcResult.getBackCashAmount().compareTo(0) > 0) {
            List<PresentStatusRecordDO> presentStatusRecords = new ArrayList<>();

            PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                    .presentType(PRESENT_TYPE_PROFIT)
                    .occurredTime(now)
                    .status(PRESENT_PROFIT_CASH_BACK_WAIT)
                    .build();
            psr.setDeleted(false);

            presentStatusRecords.add(psr);

            MerchantProfitDO profit = MerchantProfitDO.builder()
                    .accountNo(accountNo)
                    .contractNo(contractNo)
                    .profit(profitCalcResult.getBackCashAmount() * -1) // 回填保证金为支出，记录负数
                    .tradeType(TRAN_PROFIT_CASH_BACK)
                    .tradeTo(TRADE_TO_MY_CASH) // 回填保证金动向为：我的保证金
                    .presentState(PRESENT_PROFIT_CASH_BACK_WAIT) // 待回填保证金
                    .presentStatusRecords(presentStatusRecords)
                    .build();
            profit.setDeleted(false);
            profit.setRevision(0);

            profitList.add(profit);
        }

        // 本次服务费用
        if (profitCalcResult.getCosts() != null && !profitCalcResult.getCosts().isEmpty()) {
            for (CostDTO cost : profitCalcResult.getCosts()) {
                boolean isPromptPayment = cost.isPromptPayment();
                String presentState = null;
                List<PresentStatusRecordDO> presentStatusRecords = null;
                if (isPromptPayment) {
                    presentState = PRESENT_PROFIT_APPLY; // 如果费用立即付款，则需要将费用的提现状态改为申请
                    if (presentStatusRecords == null) {
                        presentStatusRecords = new ArrayList<>();

                        PresentStatusRecordDO psr = PresentStatusRecordDO.builder()
                                .presentType(PRESENT_TYPE_PROFIT)
                                .occurredTime(now)
                                .status(presentState)
                                .build();
                        psr.setDeleted(false);

                        presentStatusRecords.add(psr);
                    }
                }

                MerchantProfitDO profit = MerchantProfitDO.builder()
                        .accountNo(accountNo)
                        .contractNo(contractNo)
                        .profit(cost.getAmount() * -1) // 费用为支出，记录负数
                        .tradeType(TRAN_PROFIT_SERVICE_COST) // TODO 费用可能存在多种，交易类型是否要区分？
                        .tradeTo(TRADE_TO_MARKET) // 费用去向为：市场方
                        .presentState(presentState)
                        .presentStatusRecords(presentStatusRecords)
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
        if (profitCalcResult.getTaxes() != null && !profitCalcResult.getTaxes().isEmpty()) {
            for (TaxDTO tax : profitCalcResult.getTaxes()) {
                MerchantProfitDO profit = MerchantProfitDO.builder()
                        .accountNo(accountNo)
                        .contractNo(contractNo)
                        .profit(tax.getAmount() * -1) // 税费为支出，记录负数
                        .tradeType(TRAN_PROFIT_TAX_COST)
                        .tradeTo(TRADE_TO_MARKET) // 回填保证金动向为：市场方
                        .presentState(null) // 初始写入税费无提现状态
                        .build();
                profit.setDeleted(false);
                profit.setRevision(0);

                profitList.add(profit);
            }
        }

        return profitList;
    }

    /**
     * 支付，内部用调支付接口
     * @param bankName 银行卡账户名
     * @param bankNo 银行卡号
     * @param amount 支付金额
     */
    private void pay(String bankName, String bankNo, Integer amount) {
        // TODO: 调用支付接口

    }
}
