package com.newtouch.uctp.module.business.service.bank.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionRecordDO;
import com.newtouch.uctp.module.business.dal.mysql.TransactionRecordMapper;
import com.newtouch.uctp.module.business.service.bank.TransactionRecordService;
import org.springframework.stereotype.Service;

@Service
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecordDO> implements TransactionRecordService {
}
