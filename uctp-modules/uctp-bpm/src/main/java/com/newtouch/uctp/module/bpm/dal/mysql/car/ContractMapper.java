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

    /**
     * 根据契约锁合同ID和合同类型查找车辆合同信息
     * @param contractId     契约锁合同ID
     * @param contractType   合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    @ResultType(ContractDO.class)
    @Select("select * from uctp_contract where contract_id = #{contractId} and contract_type = #{contractType} and deleted = 0")
    ContractDO selectByContractIdAndContractType(@Param("contractId") Long contractId, @Param("contractType") Integer contractType);

    /**
     * 根据契约锁合同ID查找车辆合同信息
     * @param contractId     契约锁合同ID
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    @ResultType(ContractDO.class)
    @Select("select * from uctp_contract where contract_id = #{contractId} and deleted = 0")
    ContractDO selectByContractId(@Param("contractId") Long contractId);

    @InterceptorIgnore(tenantLine = "true")
    @ResultType(ContractDO.class)
    @Select("select * from uctp_contract where car_id = #{carId} and contract_type = #{contractType} and deleted = 0")
    ContractDO selectByCarIdAndContractType(@Param("carId") Long carId, @Param("contractType") Integer contractType);
}
