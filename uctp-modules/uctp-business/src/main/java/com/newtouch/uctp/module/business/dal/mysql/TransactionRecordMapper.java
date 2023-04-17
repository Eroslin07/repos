package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionRecordDO;
import org.apache.ibatis.annotations.Mapper;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface TransactionRecordMapper extends BaseMapperX<TransactionRecordDO> {
}
