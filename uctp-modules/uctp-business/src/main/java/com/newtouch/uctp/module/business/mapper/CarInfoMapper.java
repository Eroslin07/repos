package com.newtouch.uctp.module.business.mapper;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoPageReqVO;
import com.newtouch.uctp.module.business.domain.app.CarInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆主表 Mapper
 *
 * @author lc
 */
@Mapper
public interface CarInfoMapper extends BaseMapperX<CarInfoDO> {

    default PageResult<CarInfoDO> selectPage(AppCarInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarInfoDO>()
                .eqIfPresent(CarInfoDO::getVin, reqVO.getVin())
                .eqIfPresent(CarInfoDO::getBrand, reqVO.getBrand())
                .eqIfPresent(CarInfoDO::getYear, reqVO.getYear())
                .eqIfPresent(CarInfoDO::getStyle, reqVO.getStyle())
                .eqIfPresent(CarInfoDO::getEngineNum, reqVO.getEngineNum())
                .eqIfPresent(CarInfoDO::getVehicleReceiptAmount, reqVO.getVehicleReceiptAmount())
                .eqIfPresent(CarInfoDO::getRemarks, reqVO.getRemarks())
                .eqIfPresent(CarInfoDO::getRevision, reqVO.getRevision())
                .eqIfPresent(CarInfoDO::getCarDetailId, reqVO.getCarDetailId())
                .eqIfPresent(CarInfoDO::getBusinessId, reqVO.getBusinessId())
                .betweenIfPresent(CarInfoDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(CarInfoDO::getUpdatedTime, reqVO.getUpdatedTime())
                .eqIfPresent(CarInfoDO::getSalesStatus, reqVO.getSalesStatus())
                .eqIfPresent(CarInfoDO::getCheckStatus, reqVO.getCheckStatus())
                .orderByDesc(CarInfoDO::getId));
    }



}
