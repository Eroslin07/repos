package com.newtouch.uctp.module.business.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountEnum;
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
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
            String accountNo = generateAccountNo();
            MerchantAccountDO merchantAccount = new MerchantAccountDO();
            merchantAccount.setAccountNo(accountNo);
            merchantAccount.setMerchantId(accountDTO.getMerchantId());
            merchantAccount.setTenantId(accountDTO.getTenantId());
            save(merchantAccount);

            // 创建银行保证经充值子账号
            NominalAccountRequest requestCash = new NominalAccountRequest();
            //NominalAccountResponse cashAccountNo = transactionService.nominalAccountGenerate(requestCash);
            MerchantBankDO merchantBankCash = new MerchantBankDO();
            merchantBankCash.setAccountNo(accountNo);

            merchantBankCash.setBankNo(accountDTO.getCashBankNo());
            //todo 待换银行接口
            merchantBankCash.setChildAcctNo(generateAccountNo());
            //todo 商户编号
            merchantBankCash.setBusinessType(AccountEnum.BANK_NO_CASH.getKey());
            merchantBankService.save(merchantBankCash);


            // 创建银行对公利润提现子账号
            NominalAccountRequest requestProfit = new NominalAccountRequest();
            //NominalAccountResponse bankAccountNoProfit = transactionService.nominalAccountGenerate(requestProfit);
            MerchantBankDO merchantBankProfit = new MerchantBankDO();
            merchantBankProfit.setAccountNo(accountNo);
            merchantBankProfit.setBusinessType(AccountEnum.BANK_NO_PROFIT.getKey());
            //todo 待换银行接口
            merchantBankProfit.setChildAcctNo(generateAccountNo());
            merchantBankProfit.setBankNo(accountDTO.getBankNo());
            merchantBankProfit.setBankName(accountDTO.getBankName());
            merchantBankService.save(merchantBankProfit);
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


    /**
     * 生成平台商户虚拟账户号
     *
     * @return
     */
    private String generateAccountNo() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
