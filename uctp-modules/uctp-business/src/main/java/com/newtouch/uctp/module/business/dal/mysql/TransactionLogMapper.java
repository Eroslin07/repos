package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionLogMapper extends BaseMapperX<TransactionLogDO> {


}
