package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantPOSDeviceDO;
import org.apache.ibatis.annotations.Mapper;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface MerchantPOSDeviceDOMapper extends BaseMapperX<MerchantPOSDeviceDO> {

}
