package com.newtouch.uctp.module.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantAccountMapper;
import com.newtouch.uctp.module.business.enums.AccountEnum;
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
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

    @Override
    @Transactional
    public boolean openAccount(AccountDTO accountDTO) {
        try {
            // 创建银行保证经充值子账号
            String cashAccountNo = "";//TODO
            MerchantAccountDO accountCash = new MerchantAccountDO();
            accountCash.setAccountNo(cashAccountNo);
            accountCash.setMerchantId(accountDTO.getMerchantId());
            accountCash.setTenantId(accountDTO.getTenantId());
            save(accountCash);

            MerchantBankDO merchantBankCash = new MerchantBankDO();
            merchantBankCash.setAccountNo(cashAccountNo);
            merchantBankCash.setBusinessType(AccountEnum.BANK_NO_CASH.getKey());
            merchantBankService.save(merchantBankCash);


            // 创景银行对公利润提现子账号
            String profitAccountNo = "";//TODO
            MerchantAccountDO accountProfit = new MerchantAccountDO();
            accountProfit.setAccountNo(profitAccountNo);
            save(accountProfit);

            MerchantBankDO merchantBankProfit = new MerchantBankDO();
            merchantBankProfit.setAccountNo(cashAccountNo);
            merchantBankProfit.setBusinessType(AccountEnum.BANK_NO_CASH.getKey());
            merchantBankService.save(merchantBankProfit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
