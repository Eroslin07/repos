package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
