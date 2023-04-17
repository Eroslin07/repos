package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@InterceptorIgnore(tenantLine = "true")
@Mapper
public interface MerchantAccountMapper extends BaseMapper<MerchantAccountDO> {

    MerchantAccountDO queryByAccountNo(@Param("accountNo") String accountNo);

    int rechargeCash(@Param("accountNo") String accountNo, @Param("tranAmount") Integer tranAmount, @Param("revision") Integer revision, @Param("type") int type);

    int withdrawCash(@Param("accountNo") String accountNo, @Param("tranAmount") Integer tranAmount);

    MerchantAccountDO selectForUpdateByAccountNo(@Param("accountNo") String accountNo);
}
