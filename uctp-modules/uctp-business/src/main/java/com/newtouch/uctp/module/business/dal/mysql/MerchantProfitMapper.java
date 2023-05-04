package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.controller.app.account.vo.ProfitCostMonthRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.profit.MerchantProfitDO;
import com.newtouch.uctp.module.business.service.account.dto.ProfitPresentFormDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MerchantProfitMapper extends BaseMapperX<MerchantProfitDO> {

    List<ProfitCostMonthRespVO> selectMonthCosts(@Param("accountNo") String accountNo,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("serviceCostType") String serviceCostType,
                                                 @Param("taxCostType") String taxCostType);

    ProfitPresentFormDTO selectProfitById(Long id);
}
