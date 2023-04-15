package com.newtouch.uctp.module.business.dal.mysql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MerchantAccountMapper extends BaseMapper<MerchantAccountDO> {

    MerchantAccountDO queryByAccountNo(String accountNo);

    int rechargeCash(@Param("accountNo") String accountNo, @Param("tranAmount") Integer tranAmount);

    int withdrawCash(@Param("accountNo") String accountNo, @Param("tranAmount") Integer tranAmount);

    MerchantAccountDO selectForUpdateByAccountNo(@Param("accountNo") String accountNo);
}
