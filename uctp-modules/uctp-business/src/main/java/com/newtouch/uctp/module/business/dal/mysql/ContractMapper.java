package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同 Mapper
 */
@Mapper
public interface ContractMapper extends BaseMapperX<ContractDO> {


    List<CarDCVo> getContractIds(String contractID);

    int updateContractStatas(@Param("vo") CarDCVo carDCVo);

    List<AppContractarVO> getContractInfo(String carID);

    default  List<ContractDO> selectByCarID(Long carID){
        return selectList(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getCarId,carID)
        );
    };

    ContractDO getContractOneBuyType(@Param("carId") Long carId,
                             @Param("contractType") String contractType);

    default ContractDO selectByContractId(Long contractId){
        return selectOne(ContractDO::getContractId,contractId);
    };

    @TenantIgnore
    @Select("select * from uctp_contract where CONTRACT_ID = #{contractId} AND DELETED = 0")
    ContractDO findByContractId(@Param("contractId") Long contractId);

    default ContractDO findCollectDraft(Long carId, Integer contractType, Long tenantId){
        return selectOne(new LambdaQueryWrapperX<ContractDO>()
                .eq(ContractDO::getCarId,carId)
                .eq(ContractDO::getContractType,contractType)
                .eq(ContractDO::getTenantId,tenantId));
    }
}
