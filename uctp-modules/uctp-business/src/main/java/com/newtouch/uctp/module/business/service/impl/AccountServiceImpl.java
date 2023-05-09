package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.enums.bank.BankConstants;
import com.newtouch.uctp.module.business.enums.bank.CertificationType;
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.NominalAccountRequest;
import com.newtouch.uctp.module.business.service.bank.response.NominalAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Validated
@Slf4j
public class AccountServiceImpl extends ServiceImpl<MerchantAccountMapper, MerchantAccountDO> implements AccountService {

    @Resource
    private MerchantBankService merchantBankService;

    @Resource
    private TransactionService transactionService;

    @Resource
    private TransactionLogService transactionLogService;


    @Override
    @Transactional
    public boolean accountGenerate(AccountDTO accountDTO) {
        if (accountExists(accountDTO.getTenantId(), accountDTO.getMerchantId())) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "该商户已开立虚拟账户，不可重复开户!");
        }
        try {
            // 平台商户虚拟账号
            String accountNo = UUID.randomUUID().toString(true);
            MerchantAccountDO merchantAccount = new MerchantAccountDO();
            merchantAccount.setAccountNo(accountNo);
            merchantAccount.setMerchantId(accountDTO.getMerchantId());
            merchantAccount.setTenantId(accountDTO.getTenantId());
            save(merchantAccount);

            // 创建银行保证经充值子账号
            NominalAccountRequest requestCash = buildNominalAccountRequest(accountDTO, AccountEnum.BANK_NO_CASH.getKey());
            NominalAccountResponse bankAccountNoCash = transactionService.nominalAccountGenerate(requestCash);
            saveMerchantBank(bankAccountNoCash, AccountEnum.BANK_NO_CASH.getKey(), accountDTO, accountNo);


            // 创建银行对公利润提现子账号
            NominalAccountRequest requestProfit = buildNominalAccountRequest(accountDTO, AccountEnum.BANK_NO_PROFIT.getKey());
            NominalAccountResponse bankAccountNoProfit = transactionService.nominalAccountGenerate(requestProfit);
            saveMerchantBank(bankAccountNoProfit, AccountEnum.BANK_NO_PROFIT.getKey(), accountDTO, accountNo);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof BankException) {
                throw new BankException(((BankException) e).getRequest(), e.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean accountExists(long tenantId, long merchantId) {
        MerchantAccountDO merchantAccountDO = getOne(new LambdaQueryWrapperX<MerchantAccountDO>()
                .eq(MerchantAccountDO::getTenantId, tenantId)
                .eq(MerchantAccountDO::getMerchantId, merchantId));
        return merchantAccountDO != null;
    }

    private NominalAccountRequest buildNominalAccountRequest(AccountDTO accountDTO, String busType) {
        NominalAccountRequest request = new NominalAccountRequest();
        request.setTranDate(LocalDate.now().format(DateTimeFormatter.ofPattern(BankConstants.tranDateFormat)));
        request.setTranTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(BankConstants.tranTimeFormat)));
        request.setChannelSeqNo(UUID.randomUUID().toString(true));
        request.setAreaCode(BankConstants.AREA_CODE);
        request.setAcctNo(BankConstants.ACCT_NO);
        request.setBidsSnglFlgCd(BankConstants.ACCT_NO);
        request.setBscAcctNo(BankConstants.ACCT_NO);
        request.setAcctName(BankConstants.ACCT_NAME);
        request.setCtfType(CertificationType.ID_CARD.getCode());
        request.setCtfId(accountDTO.getIdCard());
        request.setClientName(accountDTO.getLegalRepresentative());
        if (AccountEnum.BANK_NO_PROFIT.getKey().equals(busType)) {
            request.setOpenBrNo(accountDTO.getBankNo());
            request.setOpenBranchName(accountDTO.getBankName());
        } else if (AccountEnum.BANK_NO_CASH.getKey().equals(busType)) {
            request.setOpenBrNo(accountDTO.getBankNo());
            request.setOpenBranchName(BankConstants.CASH_OPEN_BANK_NAME);
        }
        return request;
    }

    private void saveMerchantBank(NominalAccountResponse response, String busType, AccountDTO accountDTO, String accountNo) {
        MerchantBankDO merchantBankDO = new MerchantBankDO();
        merchantBankDO.setAccountNo(accountNo);
        merchantBankDO.setBusinessType(busType);
        merchantBankDO.setChildAcctNo(response.getChildAcctNo());
        if (AccountEnum.BANK_NO_PROFIT.getKey().equals(busType)) {
            merchantBankDO.setBankNo(accountDTO.getBankNo());
            merchantBankDO.setBankName(accountDTO.getBankName());
        } else if (AccountEnum.BANK_NO_CASH.getKey().equals(busType)) {
            merchantBankDO.setBankNo(accountDTO.getCashBankNo());
            merchantBankDO.setBankName(BankConstants.CASH_OPEN_BANK_NAME);
        }
        merchantBankService.save(merchantBankDO);
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString(true));
    }
}
