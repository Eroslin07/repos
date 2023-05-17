package com.newtouch.uctp.module.business.service.account.impl;

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
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

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
    @Transactional(noRollbackFor = BankException.class)
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
            saveMerchantBank( AccountEnum.BUSINESS_TYPE_CASH.getKey(), accountDTO, accountNo);
            saveMerchantBank( AccountEnum.BUSINESS_TYPE_PROFIT.getKey(), accountDTO, accountNo);
            return true;
        } catch (BankException e) {
            log.error(e.getMessage());
            throw new BankException(e.getRequest(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean accountExists(long tenantId, long merchantId) {
        MerchantAccountDO merchantAccountDO = getOne(new LambdaQueryWrapperX<MerchantAccountDO>()
                .eq(MerchantAccountDO::getTenantId, tenantId)
                .eq(MerchantAccountDO::getMerchantId, merchantId));
        return merchantAccountDO != null;
    }

    private void saveMerchantBank( String busType, AccountDTO accountDTO, String accountNo) {
        MerchantBankDO merchantBankDO = new MerchantBankDO();
        merchantBankDO.setAccountNo(accountNo);
        merchantBankDO.setBusinessType(busType);
        merchantBankDO.setChildAcctNo(UUID.randomUUID().toString(true));
        merchantBankDO.setAuthCode(UUID.randomUUID().toString(true));
        merchantBankDO.setChildAcctName(UUID.randomUUID().toString(true));
        merchantBankDO.setPcpCustNo(UUID.randomUUID().toString(true));
        if (AccountEnum.BUSINESS_TYPE_PROFIT.getKey().equals(busType)) {
            merchantBankDO.setBankNo(accountDTO.getBankNo());
            merchantBankDO.setBankName(accountDTO.getBankName());
        } else if (AccountEnum.BUSINESS_TYPE_CASH.getKey().equals(busType)) {
            merchantBankDO.setBankNo(accountDTO.getCashBankNo());
            merchantBankDO.setBankName(BankConstants.CASH_OPEN_BANK_NAME);
        }
        merchantBankService.save(merchantBankDO);
    }
}
