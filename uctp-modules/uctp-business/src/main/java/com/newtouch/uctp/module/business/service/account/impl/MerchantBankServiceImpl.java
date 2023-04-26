package com.newtouch.uctp.module.business.service.account.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantBankMapper;
import com.newtouch.uctp.module.business.service.account.MerchantBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
@Slf4j
public class MerchantBankServiceImpl extends ServiceImpl<MerchantBankMapper, MerchantBankDO> implements MerchantBankService {
    @Resource
    private MerchantBankMapper merchantBankMapper;

    @Override
    public MerchantBankDO getById(Long id) {
        if (id == null) {
            return null;
        }
        MerchantBankDO mb = merchantBankMapper.selectById(id);
        return mb;
    }
}
