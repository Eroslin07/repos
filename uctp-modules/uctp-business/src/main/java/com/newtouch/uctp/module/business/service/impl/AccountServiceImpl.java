package com.newtouch.uctp.module.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import com.newtouch.uctp.module.business.service.bank.request.NominalAccountRequest;
import com.newtouch.uctp.module.business.service.bank.response.NominalAccountResponse;
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

    @Override
    @Transactional
    public boolean accountGenerate(AccountDTO accountDTO) {
        try {

            MerchantAccountDO merchantAccount = new MerchantAccountDO();
            merchantAccount.setAccountNo(accountDTO.getIdCard());
            merchantAccount.setMerchantId(accountDTO.getMerchantId());
            merchantAccount.setTenantId(accountDTO.getTenantId());
            save(merchantAccount);

            // 创建银行保证经充值子账号
            NominalAccountRequest requestCash = new NominalAccountRequest();
            NominalAccountResponse cashAccountNo = transactionService.nominalAccountGenerate(requestCash);
            MerchantBankDO merchantBankCash = new MerchantBankDO();
            merchantBankCash.setAccountNo(accountDTO.getIdCard());
            merchantBankCash.setBankNo(cashAccountNo.getChildAcctNo());
            //todo 商户编号
            merchantBankCash.setBusinessType(AccountEnum.BANK_NO_CASH.getKey());
            merchantBankService.save(merchantBankCash);


            // 创景银行对公利润提现子账号
            NominalAccountRequest requestProfit = new NominalAccountRequest();
            NominalAccountResponse bankAccountNoProfit = transactionService.nominalAccountGenerate(requestProfit);
            MerchantBankDO merchantBankProfit = new MerchantBankDO();
            merchantBankProfit.setAccountNo(accountDTO.getIdCard());
            merchantBankProfit.setBusinessType(AccountEnum.BANK_NO_PROFIT.getKey());
            merchantBankProfit.setBankNo(bankAccountNoProfit.getChildAcctNo());
            merchantBankService.save(merchantBankProfit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成平台商户虚拟账户号
     *
     * @return
     */
    private String generateAccountNo() {
        return "";
    }
}
