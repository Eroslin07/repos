package com.newtouch.uctp.module.bpm.dal.mysql.car;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.ContractDO;

/**
 * 合同 Mapper
 */
@Mapper
public interface ContractMapper extends BaseMapperX<ContractDO> {
    @InterceptorIgnore(tenantLine = "true")
    @ResultType(Map.class)
    @Select("select * from uctp_contract where contract_id = #{contractId} and deleted = 0")
    Map<String, Object> findContractByContractIdToMap(@Param("contractId") Long contractId);
}
