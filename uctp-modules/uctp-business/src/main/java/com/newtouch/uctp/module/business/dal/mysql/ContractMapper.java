package com.newtouch.uctp.module.business.dal.mysql;


import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarCostVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.AppMyCostVO;
import com.newtouch.uctp.module.business.controller.app.myCost.vo.CostExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 合同 Mapper
 */
@Mapper
public interface ContractMapper {


    List<CarDCVo> getContractIds(String contractID);

    int updateContractStatas(@Param("vo") CarDCVo carDCVo);

    List<AppContractarVO> getContractInfo(String carID);
}
