package com.newtouch.uctp.module.business.service.account.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.dal.dataobject.account.BankBackSingleDO;
import com.newtouch.uctp.module.business.dal.mysql.BankBackSingleMapper;
import com.newtouch.uctp.module.business.service.account.BankBackSingleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankBackSingleServiceImpl extends ServiceImpl<BankBackSingleMapper, BankBackSingleDO> implements BankBackSingleService {

}
