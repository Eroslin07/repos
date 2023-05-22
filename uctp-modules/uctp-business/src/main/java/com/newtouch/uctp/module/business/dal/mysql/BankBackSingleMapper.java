package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.account.BankBackSingleDO;
import org.apache.ibatis.annotations.Mapper;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface BankBackSingleMapper extends BaseMapperX<BankBackSingleDO> {
}
