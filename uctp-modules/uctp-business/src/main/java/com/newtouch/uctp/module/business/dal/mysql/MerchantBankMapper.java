package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantBankDO;
import org.apache.ibatis.annotations.Mapper;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface MerchantBankMapper extends BaseMapper<MerchantBankDO> {

}
