package com.newtouch.uctp.module.business.service.bank.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import com.newtouch.uctp.module.business.dal.mysql.TransactionLogMapper;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogServiceImpl extends ServiceImpl<TransactionLogMapper, TransactionLogDO> implements TransactionLogService {
}
