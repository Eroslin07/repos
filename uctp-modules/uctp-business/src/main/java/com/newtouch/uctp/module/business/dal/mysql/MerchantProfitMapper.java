package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MerchantProfitMapper extends BaseMapperX<MerchantProfitDO> {

    void insertBatchProfit(@Param("profitList") List<MerchantProfitDO> profitList);
}
